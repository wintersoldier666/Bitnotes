package com.bitnotes.app.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.nio.ByteBuffer
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Core cryptographic manager using AES-256-GCM encryption.
 *
 * Architecture:
 * - Notes are encrypted with a Data Encryption Key (DEK)
 * - The DEK is encrypted with a Key Encryption Key (KEK) derived from user PIN
 * - The KEK is also stored in Android Keystore for biometric unlock
 *
 * This provides:
 * - AES-256-GCM encryption (authenticated encryption with 128-bit auth tag)
 * - PBKDF2WithHmacSHA256 key derivation (310,000 iterations per OWASP 2023)
 * - Unique IV per encryption operation
 * - Android Keystore hardware-backed key storage
 */
@Singleton
class CryptoManager @Inject constructor() {

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEYSTORE_ALIAS = "BitnotesMasterKey"
        private const val AES_GCM_NO_PADDING = "AES/GCM/NoPadding"
        private const val KEY_SIZE_BITS = 256
        private const val GCM_TAG_LENGTH = 128
        private const val GCM_IV_LENGTH = 12
        private const val SALT_LENGTH = 32
        private const val PBKDF2_ITERATIONS = 310_000
        private const val PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256"
    }

    private val secureRandom = SecureRandom()
    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

    // ───────────── Android Keystore Operations ─────────────

    /**
     * Generates or retrieves the hardware-backed master key in Android Keystore.
     * Used for biometric-protected encryption.
     */
    fun getOrCreateKeystoreKey(): SecretKey {
        if (keyStore.containsAlias(KEYSTORE_ALIAS)) {
            return (keyStore.getEntry(KEYSTORE_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
        }

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )

        val spec = KeyGenParameterSpec.Builder(
            KEYSTORE_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setKeySize(KEY_SIZE_BITS)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(false) // We handle auth ourselves
            .setRandomizedEncryptionRequired(true)
            .build()

        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    /**
     * Deletes the keystore key (for reset/wipe operations).
     */
    fun deleteKeystoreKey() {
        if (keyStore.containsAlias(KEYSTORE_ALIAS)) {
            keyStore.deleteEntry(KEYSTORE_ALIAS)
        }
    }

    // ───────────── AES-256-GCM Encryption ─────────────

    /**
     * Encrypts data using AES-256-GCM.
     * Output format: [IV (12 bytes)] + [Ciphertext + Auth Tag]
     */
    fun encrypt(plaintext: ByteArray, key: SecretKey): ByteArray {
        val cipher = Cipher.getInstance(AES_GCM_NO_PADDING)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val ciphertext = cipher.doFinal(plaintext)

        // Prepend IV to ciphertext
        return ByteBuffer.allocate(iv.size + ciphertext.size)
            .put(iv)
            .put(ciphertext)
            .array()
    }

    /**
     * Decrypts AES-256-GCM encrypted data.
     * Expects format: [IV (12 bytes)] + [Ciphertext + Auth Tag]
     */
    fun decrypt(encryptedData: ByteArray, key: SecretKey): ByteArray {
        val buffer = ByteBuffer.wrap(encryptedData)
        val iv = ByteArray(GCM_IV_LENGTH)
        buffer.get(iv)
        val ciphertext = ByteArray(buffer.remaining())
        buffer.get(ciphertext)

        val cipher = Cipher.getInstance(AES_GCM_NO_PADDING)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, spec)
        return cipher.doFinal(ciphertext)
    }

    /**
     * Encrypts a string and returns Base64-encoded result.
     */
    fun encryptString(plaintext: String, key: SecretKey): String {
        val encrypted = encrypt(plaintext.toByteArray(Charsets.UTF_8), key)
        return android.util.Base64.encodeToString(encrypted, android.util.Base64.NO_WRAP)
    }

    /**
     * Decrypts a Base64-encoded encrypted string.
     */
    fun decryptString(encryptedBase64: String, key: SecretKey): String {
        val encrypted = android.util.Base64.decode(encryptedBase64, android.util.Base64.NO_WRAP)
        return String(decrypt(encrypted, key), Charsets.UTF_8)
    }

    // ───────────── Key Derivation (PIN-based) ─────────────

    /**
     * Derives a 256-bit AES key from a PIN using PBKDF2.
     * Uses 310,000 iterations (OWASP 2023 recommendation).
     */
    fun deriveKeyFromPin(pin: String, salt: ByteArray): SecretKey {
        val factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM)
        val spec = PBEKeySpec(
            pin.toCharArray(),
            salt,
            PBKDF2_ITERATIONS,
            KEY_SIZE_BITS
        )
        val tmp = factory.generateSecret(spec)
        spec.clearPassword() // Clear sensitive data from memory
        return SecretKeySpec(tmp.encoded, "AES")
    }

    /**
     * Generates a cryptographically secure random salt.
     */
    fun generateSalt(): ByteArray {
        val salt = ByteArray(SALT_LENGTH)
        secureRandom.nextBytes(salt)
        return salt
    }

    /**
     * Generates a cryptographically secure random AES-256 key.
     * This is the Data Encryption Key (DEK) used to encrypt notes.
     */
    fun generateDataKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(KEY_SIZE_BITS, secureRandom)
        return keyGenerator.generateKey()
    }

    /**
     * Wraps (encrypts) a key with another key for key wrapping.
     */
    fun wrapKey(keyToWrap: SecretKey, wrappingKey: SecretKey): ByteArray {
        return encrypt(keyToWrap.encoded, wrappingKey)
    }

    /**
     * Unwraps (decrypts) a wrapped key.
     */
    fun unwrapKey(wrappedKey: ByteArray, wrappingKey: SecretKey): SecretKey {
        val keyBytes = decrypt(wrappedKey, wrappingKey)
        return SecretKeySpec(keyBytes, "AES")
    }

    // ───────────── Backup Encryption ─────────────

    /**
     * Encrypts backup data with a password-derived key.
     * Output format: [Salt (32)] + [IV (12)] + [Ciphertext + Auth Tag]
     */
    fun encryptBackup(data: ByteArray, password: String): ByteArray {
        val salt = generateSalt()
        val key = deriveKeyFromPin(password, salt)
        val encrypted = encrypt(data, key)

        return ByteBuffer.allocate(salt.size + encrypted.size)
            .put(salt)
            .put(encrypted)
            .array()
    }

    /**
     * Decrypts backup data using password-derived key.
     */
    fun decryptBackup(encryptedData: ByteArray, password: String): ByteArray {
        val buffer = ByteBuffer.wrap(encryptedData)
        val salt = ByteArray(SALT_LENGTH)
        buffer.get(salt)
        val ciphertext = ByteArray(buffer.remaining())
        buffer.get(ciphertext)

        val key = deriveKeyFromPin(password, salt)
        return decrypt(ciphertext, key)
    }

    /**
     * Securely wipes a byte array from memory.
     */
    fun wipeByteArray(array: ByteArray) {
        array.fill(0)
    }

    /**
     * Securely wipes a char array from memory.
     */
    fun wipeCharArray(array: CharArray) {
        array.fill('\u0000')
    }
}
