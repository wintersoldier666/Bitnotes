package com.bitnotes.app.di

import android.content.Context
import com.bitnotes.app.crypto.CryptoManager
import com.bitnotes.app.crypto.KeyManager
import com.bitnotes.app.data.db.DatabaseManager
import com.bitnotes.app.data.repository.BackupRepository
import com.bitnotes.app.data.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCryptoManager(): CryptoManager = CryptoManager()

    @Provides
    @Singleton
    fun provideKeyManager(
        @ApplicationContext context: Context,
        cryptoManager: CryptoManager
    ): KeyManager = KeyManager(context, cryptoManager)

    @Provides
    @Singleton
    fun provideDatabaseManager(
        @ApplicationContext context: Context
    ): DatabaseManager = DatabaseManager(context)

    @Provides
    @Singleton
    fun provideNoteRepository(
        databaseManager: DatabaseManager,
        cryptoManager: CryptoManager,
        keyManager: KeyManager
    ): NoteRepository = NoteRepository(databaseManager, cryptoManager, keyManager)

    @Provides
    @Singleton
    fun provideBackupRepository(
        @ApplicationContext context: Context,
        noteRepository: NoteRepository,
        cryptoManager: CryptoManager
    ): BackupRepository = BackupRepository(context, noteRepository, cryptoManager)
}
