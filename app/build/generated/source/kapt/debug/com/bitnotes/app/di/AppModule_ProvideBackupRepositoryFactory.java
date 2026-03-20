package com.bitnotes.app.di;

import android.content.Context;
import com.bitnotes.app.crypto.CryptoManager;
import com.bitnotes.app.data.repository.BackupRepository;
import com.bitnotes.app.data.repository.NoteRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideBackupRepositoryFactory implements Factory<BackupRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<NoteRepository> noteRepositoryProvider;

  private final Provider<CryptoManager> cryptoManagerProvider;

  public AppModule_ProvideBackupRepositoryFactory(Provider<Context> contextProvider,
      Provider<NoteRepository> noteRepositoryProvider,
      Provider<CryptoManager> cryptoManagerProvider) {
    this.contextProvider = contextProvider;
    this.noteRepositoryProvider = noteRepositoryProvider;
    this.cryptoManagerProvider = cryptoManagerProvider;
  }

  @Override
  public BackupRepository get() {
    return provideBackupRepository(contextProvider.get(), noteRepositoryProvider.get(), cryptoManagerProvider.get());
  }

  public static AppModule_ProvideBackupRepositoryFactory create(Provider<Context> contextProvider,
      Provider<NoteRepository> noteRepositoryProvider,
      Provider<CryptoManager> cryptoManagerProvider) {
    return new AppModule_ProvideBackupRepositoryFactory(contextProvider, noteRepositoryProvider, cryptoManagerProvider);
  }

  public static BackupRepository provideBackupRepository(Context context,
      NoteRepository noteRepository, CryptoManager cryptoManager) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideBackupRepository(context, noteRepository, cryptoManager));
  }
}
