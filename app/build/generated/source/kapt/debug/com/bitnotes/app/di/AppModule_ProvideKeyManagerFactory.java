package com.bitnotes.app.di;

import android.content.Context;
import com.bitnotes.app.crypto.CryptoManager;
import com.bitnotes.app.crypto.KeyManager;
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
public final class AppModule_ProvideKeyManagerFactory implements Factory<KeyManager> {
  private final Provider<Context> contextProvider;

  private final Provider<CryptoManager> cryptoManagerProvider;

  public AppModule_ProvideKeyManagerFactory(Provider<Context> contextProvider,
      Provider<CryptoManager> cryptoManagerProvider) {
    this.contextProvider = contextProvider;
    this.cryptoManagerProvider = cryptoManagerProvider;
  }

  @Override
  public KeyManager get() {
    return provideKeyManager(contextProvider.get(), cryptoManagerProvider.get());
  }

  public static AppModule_ProvideKeyManagerFactory create(Provider<Context> contextProvider,
      Provider<CryptoManager> cryptoManagerProvider) {
    return new AppModule_ProvideKeyManagerFactory(contextProvider, cryptoManagerProvider);
  }

  public static KeyManager provideKeyManager(Context context, CryptoManager cryptoManager) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideKeyManager(context, cryptoManager));
  }
}
