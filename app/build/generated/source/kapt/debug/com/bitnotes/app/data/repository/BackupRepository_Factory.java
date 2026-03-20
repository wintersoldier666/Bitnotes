package com.bitnotes.app.data.repository;

import android.content.Context;
import com.bitnotes.app.crypto.CryptoManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class BackupRepository_Factory implements Factory<BackupRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<NoteRepository> noteRepositoryProvider;

  private final Provider<CryptoManager> cryptoManagerProvider;

  public BackupRepository_Factory(Provider<Context> contextProvider,
      Provider<NoteRepository> noteRepositoryProvider,
      Provider<CryptoManager> cryptoManagerProvider) {
    this.contextProvider = contextProvider;
    this.noteRepositoryProvider = noteRepositoryProvider;
    this.cryptoManagerProvider = cryptoManagerProvider;
  }

  @Override
  public BackupRepository get() {
    return newInstance(contextProvider.get(), noteRepositoryProvider.get(), cryptoManagerProvider.get());
  }

  public static BackupRepository_Factory create(Provider<Context> contextProvider,
      Provider<NoteRepository> noteRepositoryProvider,
      Provider<CryptoManager> cryptoManagerProvider) {
    return new BackupRepository_Factory(contextProvider, noteRepositoryProvider, cryptoManagerProvider);
  }

  public static BackupRepository newInstance(Context context, NoteRepository noteRepository,
      CryptoManager cryptoManager) {
    return new BackupRepository(context, noteRepository, cryptoManager);
  }
}
