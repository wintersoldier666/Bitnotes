package com.bitnotes.app.ui.auth;

import com.bitnotes.app.crypto.KeyManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<KeyManager> keyManagerProvider;

  public AuthViewModel_Factory(Provider<KeyManager> keyManagerProvider) {
    this.keyManagerProvider = keyManagerProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(keyManagerProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<KeyManager> keyManagerProvider) {
    return new AuthViewModel_Factory(keyManagerProvider);
  }

  public static AuthViewModel newInstance(KeyManager keyManager) {
    return new AuthViewModel(keyManager);
  }
}
