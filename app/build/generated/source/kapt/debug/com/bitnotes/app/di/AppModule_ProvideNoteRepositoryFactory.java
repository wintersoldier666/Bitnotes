package com.bitnotes.app.di;

import com.bitnotes.app.crypto.CryptoManager;
import com.bitnotes.app.crypto.KeyManager;
import com.bitnotes.app.data.db.DatabaseManager;
import com.bitnotes.app.data.repository.NoteRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class AppModule_ProvideNoteRepositoryFactory implements Factory<NoteRepository> {
  private final Provider<DatabaseManager> databaseManagerProvider;

  private final Provider<CryptoManager> cryptoManagerProvider;

  private final Provider<KeyManager> keyManagerProvider;

  public AppModule_ProvideNoteRepositoryFactory(Provider<DatabaseManager> databaseManagerProvider,
      Provider<CryptoManager> cryptoManagerProvider, Provider<KeyManager> keyManagerProvider) {
    this.databaseManagerProvider = databaseManagerProvider;
    this.cryptoManagerProvider = cryptoManagerProvider;
    this.keyManagerProvider = keyManagerProvider;
  }

  @Override
  public NoteRepository get() {
    return provideNoteRepository(databaseManagerProvider.get(), cryptoManagerProvider.get(), keyManagerProvider.get());
  }

  public static AppModule_ProvideNoteRepositoryFactory create(
      Provider<DatabaseManager> databaseManagerProvider,
      Provider<CryptoManager> cryptoManagerProvider, Provider<KeyManager> keyManagerProvider) {
    return new AppModule_ProvideNoteRepositoryFactory(databaseManagerProvider, cryptoManagerProvider, keyManagerProvider);
  }

  public static NoteRepository provideNoteRepository(DatabaseManager databaseManager,
      CryptoManager cryptoManager, KeyManager keyManager) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideNoteRepository(databaseManager, cryptoManager, keyManager));
  }
}
