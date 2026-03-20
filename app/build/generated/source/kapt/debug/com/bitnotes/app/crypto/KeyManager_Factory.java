package com.bitnotes.app.crypto;

import android.content.Context;
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
public final class KeyManager_Factory implements Factory<KeyManager> {
  private final Provider<Context> contextProvider;

  private final Provider<CryptoManager> cryptoManagerProvider;

  public KeyManager_Factory(Provider<Context> contextProvider,
      Provider<CryptoManager> cryptoManagerProvider) {
    this.contextProvider = contextProvider;
    this.cryptoManagerProvider = cryptoManagerProvider;
  }

  @Override
  public KeyManager get() {
    return newInstance(contextProvider.get(), cryptoManagerProvider.get());
  }

  public static KeyManager_Factory create(Provider<Context> contextProvider,
      Provider<CryptoManager> cryptoManagerProvider) {
    return new KeyManager_Factory(contextProvider, cryptoManagerProvider);
  }

  public static KeyManager newInstance(Context context, CryptoManager cryptoManager) {
    return new KeyManager(context, cryptoManager);
  }
}
