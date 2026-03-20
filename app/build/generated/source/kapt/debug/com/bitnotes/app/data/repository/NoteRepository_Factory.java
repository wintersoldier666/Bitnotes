package com.bitnotes.app.data.repository;

import com.bitnotes.app.crypto.CryptoManager;
import com.bitnotes.app.crypto.KeyManager;
import com.bitnotes.app.data.db.DatabaseManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class NoteRepository_Factory implements Factory<NoteRepository> {
  private final Provider<DatabaseManager> databaseManagerProvider;

  private final Provider<CryptoManager> cryptoManagerProvider;

  private final Provider<KeyManager> keyManagerProvider;

  public NoteRepository_Factory(Provider<DatabaseManager> databaseManagerProvider,
      Provider<CryptoManager> cryptoManagerProvider, Provider<KeyManager> keyManagerProvider) {
    this.databaseManagerProvider = databaseManagerProvider;
    this.cryptoManagerProvider = cryptoManagerProvider;
    this.keyManagerProvider = keyManagerProvider;
  }

  @Override
  public NoteRepository get() {
    return newInstance(databaseManagerProvider.get(), cryptoManagerProvider.get(), keyManagerProvider.get());
  }

  public static NoteRepository_Factory create(Provider<DatabaseManager> databaseManagerProvider,
      Provider<CryptoManager> cryptoManagerProvider, Provider<KeyManager> keyManagerProvider) {
    return new NoteRepository_Factory(databaseManagerProvider, cryptoManagerProvider, keyManagerProvider);
  }

  public static NoteRepository newInstance(DatabaseManager databaseManager,
      CryptoManager cryptoManager, KeyManager keyManager) {
    return new NoteRepository(databaseManager, cryptoManager, keyManager);
  }
}
