package com.bitnotes.app.di;

import android.content.Context;
import com.bitnotes.app.crypto.CryptoManager;
import com.bitnotes.app.crypto.KeyManager;
import com.bitnotes.app.data.db.DatabaseManager;
import com.bitnotes.app.data.repository.BackupRepository;
import com.bitnotes.app.data.repository.NoteRepository;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\"\u0010\u0003\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007J\b\u0010\u000b\u001a\u00020\nH\u0007J\u0012\u0010\f\u001a\u00020\r2\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u001a\u0010\u000e\u001a\u00020\u000f2\b\b\u0001\u0010\u0005\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0007J \u0010\u0010\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u000fH\u0007\u00a8\u0006\u0013"}, d2 = {"Lcom/bitnotes/app/di/AppModule;", "", "()V", "provideBackupRepository", "Lcom/bitnotes/app/data/repository/BackupRepository;", "context", "Landroid/content/Context;", "noteRepository", "Lcom/bitnotes/app/data/repository/NoteRepository;", "cryptoManager", "Lcom/bitnotes/app/crypto/CryptoManager;", "provideCryptoManager", "provideDatabaseManager", "Lcom/bitnotes/app/data/db/DatabaseManager;", "provideKeyManager", "Lcom/bitnotes/app/crypto/KeyManager;", "provideNoteRepository", "databaseManager", "keyManager", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class AppModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.bitnotes.app.di.AppModule INSTANCE = null;
    
    private AppModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.bitnotes.app.crypto.CryptoManager provideCryptoManager() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.bitnotes.app.crypto.KeyManager provideKeyManager(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.bitnotes.app.crypto.CryptoManager cryptoManager) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.bitnotes.app.data.db.DatabaseManager provideDatabaseManager(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.bitnotes.app.data.repository.NoteRepository provideNoteRepository(@org.jetbrains.annotations.NotNull()
    com.bitnotes.app.data.db.DatabaseManager databaseManager, @org.jetbrains.annotations.NotNull()
    com.bitnotes.app.crypto.CryptoManager cryptoManager, @org.jetbrains.annotations.NotNull()
    com.bitnotes.app.crypto.KeyManager keyManager) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.bitnotes.app.data.repository.BackupRepository provideBackupRepository(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.bitnotes.app.data.repository.NoteRepository noteRepository, @org.jetbrains.annotations.NotNull()
    com.bitnotes.app.crypto.CryptoManager cryptoManager) {
        return null;
    }
}