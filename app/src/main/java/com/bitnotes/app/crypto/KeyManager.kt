package com.bitnotes.app.crypto

import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

private val Context.secureDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "bitnotes_secure_config"
)

/**
 * Manages encryption keys for the app.
 *
 * Key hierarchy:
 * 1. PIN → PBKDF2 → Key Encryption Key (KEK)
 * 2. KEK encrypts the Data Encryption Key (DEK)
 * 3. DEK is used to encrypt all notes
 *
 * This allows:
 * - PIN change without re-encrypting all notes (just re-wrap the DEK)
 * - Biometric unlock (store KEK encrypted with keystore key)
 */
@Singleton
class KeyManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cryptoManager: CryptoManager
) {

    companion object {
        private val KEY_SALT = stringPreferencesKey("key_salt")
        private val KEY_WRAPPED_DEK = stringPreferencesKey("wrapped_dek")
        private val KEY_PIN_HASH = stringPreferencesKey("pin_hash")
        private val KEY_BIOMETRIC_WRAPPED_KEK = stringPreferencesKey("biometric_wrapped_kek")
        private val KEY_IS_SETUP = booleanPreferencesKey("is_setup")
        private val KEY_FAILED_ATTEMPTS = stringPreferencesKey("failed_attempts")
        private val KEY_LOCKOUT_TIME = stringPreferencesKey("lockout_time")
        private val KEY_BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")

        const val MAX_ATTEMPTS = 5
        const val LOCKOUT_DURATION_MS = 30_000L // 30 seconds
    }

    // In-memory cache of the DEK (cleared on app backgrounding)
    @Volatile
    private var cachedDek: SecretKey? = null

    // ───────────── Setup & Authentication ─────────────

    /**
     * Sets up the app with a new PIN. Creates the DEK and encrypts it.
     */
    suspend fun setupWithPin(pin: String) {
        val salt = cryptoManager.generateSalt()
        val kek = cryptoManager.deriveKeyFromPin(pin, salt)
        val dek = cryptoManager.generateDataKey()
        val wrappedDek = cryptoManager.wrapKey(dek, kek)
        val pinHash = hashPin(pin, salt)

        context.secureDataStore.edit { prefs ->
            prefs[KEY_SALT] = Base64.encodeToString(salt, Base64.NO_WRAP)
            prefs[KEY_WRAPPED_DEK] = Base64.encodeToString(wrappedDek, Base64.NO_WRAP)
            prefs[KEY_PIN_HASH] = pinHash
            prefs[KEY_IS_SETUP] = true
            prefs[KEY_FAILED_ATTEMPTS] = "0"
        }

        cachedDek = dek
    }

    /**
     * Unlocks with PIN, returns true if successful.
     */
    suspend fun unlockWithPin(pin: String): Boolean {
        if (isLockedOut()) return false

        val prefs = context.secureDataStore.data.first()
        val storedHash = prefs[KEY_PIN_HASH] ?: return false
        val salt = Base64.decode(prefs[KEY_SALT] ?: return false, Base64.NO_WRAP)
        val pinHash = hashPin(pin, salt)

        return if (pinHash == storedHash) {
            val kek = cryptoManager.deriveKeyFromPin(pin, salt)
            val wrappedDek = Base64.decode(prefs[KEY_WRAPPED_DEK] ?: return false, Base64.NO_WRAP)
            cachedDek = cryptoManager.unwrapKey(wrappedDek, kek)
            resetFailedAttempts()
            true
        } else {
            incrementFailedAttempts()
            false
        }
    }

    /**
     * Changes the PIN without re-encrypting all notes.
     * Only re-wraps the DEK with the new KEK.
     */
    suspend fun changePin(currentPin: String, newPin: String): Boolean {
        val prefs = context.secureDataStore.data.first()
        val storedHash = prefs[KEY_PIN_HASH] ?: return false
        val oldSalt = Base64.decode(prefs[KEY_SALT] ?: return false, Base64.NO_WRAP)

        if (hashPin(currentPin, oldSalt) != storedHash) return false

        val dek = cachedDek ?: return false
        val newSalt = cryptoManager.generateSalt()
        val newKek = cryptoManager.deriveKeyFromPin(newPin, newSalt)
        val newWrappedDek = cryptoManager.wrapKey(dek, newKek)
        val newPinHash = hashPin(newPin, newSalt)

        context.secureDataStore.edit { p ->
            p[KEY_SALT] = Base64.encodeToString(newSalt, Base64.NO_WRAP)
            p[KEY_WRAPPED_DEK] = Base64.encodeToString(newWrappedDek, Base64.NO_WRAP)
            p[KEY_PIN_HASH] = newPinHash
        }

        return true
    }

    // ───────────── Biometric Support ─────────────

    /**
     * Enables biometric unlock by encrypting the KEK with the keystore key.
     */
    suspend fun enableBiometric(pin: String) {
        val prefs = context.secureDataStore.data.first()
        val salt = Base64.decode(prefs[KEY_SALT] ?: return, Base64.NO_WRAP)
        val kek = cryptoManager.deriveKeyFromPin(pin, salt)
        val keystoreKey = cryptoManager.getOrCreateKeystoreKey()
        val wrappedKek = cryptoManager.wrapKey(kek, keystoreKey)

        context.secureDataStore.edit { p ->
            p[KEY_BIOMETRIC_WRAPPED_KEK] = Base64.encodeToString(wrappedKek, Base64.NO_WRAP)
            p[KEY_BIOMETRIC_ENABLED] = true
        }
    }

    /**
     * Unlocks using biometric authentication (after biometric success).
     */
    suspend fun unlockWithBiometric(): Boolean {
        return try {
            val prefs = context.secureDataStore.data.first()
            val wrappedKek = Base64.decode(
                prefs[KEY_BIOMETRIC_WRAPPED_KEK] ?: return false,
                Base64.NO_WRAP
            )
            val wrappedDek = Base64.decode(
                prefs[KEY_WRAPPED_DEK] ?: return false,
                Base64.NO_WRAP
            )
            val keystoreKey = cryptoManager.getOrCreateKeystoreKey()
            val kek = cryptoManager.unwrapKey(wrappedKek, keystoreKey)
            cachedDek = cryptoManager.unwrapKey(wrappedDek, kek)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun isBiometricEnabled(): Boolean {
        return context.secureDataStore.data.map { it[KEY_BIOMETRIC_ENABLED] ?: false }.first()
    }

    suspend fun disableBiometric() {
        context.secureDataStore.edit { prefs ->
            prefs.remove(KEY_BIOMETRIC_WRAPPED_KEK)
            prefs[KEY_BIOMETRIC_ENABLED] = false
        }
    }

    // ───────────── DEK Access ─────────────

    /**
     * Returns the cached DEK for encryption/decryption.
     * Returns null if app is locked.
     */
    fun getDataEncryptionKey(): SecretKey? = cachedDek

    /**
     * Locks the app by clearing the DEK from memory.
     */
    fun lock() {
        cachedDek = null
    }

    fun isUnlocked(): Boolean = cachedDek != null

    // ───────────── Setup State ─────────────

    suspend fun isSetup(): Boolean {
        return context.secureDataStore.data.map { it[KEY_IS_SETUP] ?: false }.first()
    }

    /**
     * Completely wipes all data and keys. IRREVERSIBLE.
     */
    suspend fun wipeAll() {
        context.secureDataStore.edit { it.clear() }
        cryptoManager.deleteKeystoreKey()
        cachedDek = null
    }

    // ───────────── Brute Force Protection ─────────────

    private suspend fun incrementFailedAttempts() {
        val current = getFailedAttempts()
        val newCount = current + 1
        val lockoutTime = if (newCount >= MAX_ATTEMPTS) System.currentTimeMillis() else 0L

        context.secureDataStore.edit { prefs ->
            prefs[KEY_FAILED_ATTEMPTS] = newCount.toString()
            if (lockoutTime > 0) {
                prefs[KEY_LOCKOUT_TIME] = lockoutTime.toString()
            }
        }
    }

    private suspend fun resetFailedAttempts() {
        context.secureDataStore.edit { prefs ->
            prefs[KEY_FAILED_ATTEMPTS] = "0"
            prefs.remove(KEY_LOCKOUT_TIME)
        }
    }

    suspend fun getFailedAttempts(): Int {
        return context.secureDataStore.data.map {
            it[KEY_FAILED_ATTEMPTS]?.toIntOrNull() ?: 0
        }.first()
    }

    suspend fun isLockedOut(): Boolean {
        val prefs = context.secureDataStore.data.first()
        val lockoutTime = prefs[KEY_LOCKOUT_TIME]?.toLongOrNull() ?: return false
        val elapsed = System.currentTimeMillis() - lockoutTime
        if (elapsed >= LOCKOUT_DURATION_MS) {
            // Reset lockout after duration
            context.secureDataStore.edit { p ->
                p[KEY_FAILED_ATTEMPTS] = "0"
                p.remove(KEY_LOCKOUT_TIME)
            }
            return false
        }
        return true
    }

    suspend fun getLockoutRemainingMs(): Long {
        val prefs = context.secureDataStore.data.first()
        val lockoutTime = prefs[KEY_LOCKOUT_TIME]?.toLongOrNull() ?: return 0L
        val elapsed = System.currentTimeMillis() - lockoutTime
        return maxOf(0L, LOCKOUT_DURATION_MS - elapsed)
    }

    // ───────────── Private Helpers ─────────────

    private fun hashPin(pin: String, salt: ByteArray): String {
        // Use PBKDF2 for PIN verification too (different iteration count for speed)
        val factory = javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = javax.crypto.spec.PBEKeySpec(pin.toCharArray(), salt, 10_000, 256)
        val hash = factory.generateSecret(spec).encoded
        spec.clearPassword()
        return Base64.encodeToString(hash, Base64.NO_WRAP)
    }
}
