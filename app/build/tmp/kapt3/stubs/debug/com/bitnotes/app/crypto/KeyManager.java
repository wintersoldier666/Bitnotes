package com.bitnotes.app.crypto;

import android.content.Context;
import android.util.Base64;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import dagger.hilt.android.qualifiers.ApplicationContext;
import javax.crypto.SecretKey;
import javax.inject.Inject;
import javax.inject.Singleton;

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
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\r\b\u0007\u0018\u0000 (2\u00020\u0001:\u0001(B\u0019\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u000e\u0010\u000f\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0014J\b\u0010\u0015\u001a\u0004\u0018\u00010\bJ\u000e\u0010\u0016\u001a\u00020\u0017H\u0086@\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u0018\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010\u0011J\u0018\u0010\u001a\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\f2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u000e\u0010\u001d\u001a\u00020\u0010H\u0082@\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u001e\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u001f\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010 \u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0011J\u0006\u0010!\u001a\u00020\nJ\u0006\u0010\"\u001a\u00020\u0010J\u000e\u0010#\u001a\u00020\u0010H\u0082@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010$\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0014J\u000e\u0010%\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010&\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0014J\u000e\u0010\'\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0011R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2 = {"Lcom/bitnotes/app/crypto/KeyManager;", "", "context", "Landroid/content/Context;", "cryptoManager", "Lcom/bitnotes/app/crypto/CryptoManager;", "(Landroid/content/Context;Lcom/bitnotes/app/crypto/CryptoManager;)V", "cachedDek", "Ljavax/crypto/SecretKey;", "changePin", "", "currentPin", "", "newPin", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "disableBiometric", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "enableBiometric", "pin", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDataEncryptionKey", "getFailedAttempts", "", "getLockoutRemainingMs", "", "hashPin", "salt", "", "incrementFailedAttempts", "isBiometricEnabled", "isLockedOut", "isSetup", "isUnlocked", "lock", "resetFailedAttempts", "setupWithPin", "unlockWithBiometric", "unlockWithPin", "wipeAll", "Companion", "app_debug"})
public final class KeyManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bitnotes.app.crypto.CryptoManager cryptoManager = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_SALT = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_WRAPPED_DEK = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_PIN_HASH = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_BIOMETRIC_WRAPPED_KEK = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> KEY_IS_SETUP = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_FAILED_ATTEMPTS = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_LOCKOUT_TIME = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> KEY_BIOMETRIC_ENABLED = null;
    public static final int MAX_ATTEMPTS = 5;
    public static final long LOCKOUT_DURATION_MS = 30000L;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile javax.crypto.SecretKey cachedDek;
    @org.jetbrains.annotations.NotNull()
    public static final com.bitnotes.app.crypto.KeyManager.Companion Companion = null;
    
    @javax.inject.Inject()
    public KeyManager(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.bitnotes.app.crypto.CryptoManager cryptoManager) {
        super();
    }
    
    /**
     * Sets up the app with a new PIN. Creates the DEK and encrypts it.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setupWithPin(@org.jetbrains.annotations.NotNull()
    java.lang.String pin, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Unlocks with PIN, returns true if successful.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object unlockWithPin(@org.jetbrains.annotations.NotNull()
    java.lang.String pin, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    /**
     * Changes the PIN without re-encrypting all notes.
     * Only re-wraps the DEK with the new KEK.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object changePin(@org.jetbrains.annotations.NotNull()
    java.lang.String currentPin, @org.jetbrains.annotations.NotNull()
    java.lang.String newPin, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    /**
     * Enables biometric unlock by encrypting the KEK with the keystore key.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object enableBiometric(@org.jetbrains.annotations.NotNull()
    java.lang.String pin, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Unlocks using biometric authentication (after biometric success).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object unlockWithBiometric(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object isBiometricEnabled(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object disableBiometric(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Returns the cached DEK for encryption/decryption.
     * Returns null if app is locked.
     */
    @org.jetbrains.annotations.Nullable()
    public final javax.crypto.SecretKey getDataEncryptionKey() {
        return null;
    }
    
    /**
     * Locks the app by clearing the DEK from memory.
     */
    public final void lock() {
    }
    
    public final boolean isUnlocked() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object isSetup(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    /**
     * Completely wipes all data and keys. IRREVERSIBLE.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object wipeAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object incrementFailedAttempts(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object resetFailedAttempts(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getFailedAttempts(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object isLockedOut(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getLockoutRemainingMs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    private final java.lang.String hashPin(java.lang.String pin, byte[] salt) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/bitnotes/app/crypto/KeyManager$Companion;", "", "()V", "KEY_BIOMETRIC_ENABLED", "Landroidx/datastore/preferences/core/Preferences$Key;", "", "KEY_BIOMETRIC_WRAPPED_KEK", "", "KEY_FAILED_ATTEMPTS", "KEY_IS_SETUP", "KEY_LOCKOUT_TIME", "KEY_PIN_HASH", "KEY_SALT", "KEY_WRAPPED_DEK", "LOCKOUT_DURATION_MS", "", "MAX_ATTEMPTS", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}