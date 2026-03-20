package com.bitnotes.app.ui.notes;

import com.bitnotes.app.crypto.KeyManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<KeyManager> keyManagerProvider;

  public MainActivity_MembersInjector(Provider<KeyManager> keyManagerProvider) {
    this.keyManagerProvider = keyManagerProvider;
  }

  public static MembersInjector<MainActivity> create(Provider<KeyManager> keyManagerProvider) {
    return new MainActivity_MembersInjector(keyManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectKeyManager(instance, keyManagerProvider.get());
  }

  @InjectedFieldSignature("com.bitnotes.app.ui.notes.MainActivity.keyManager")
  public static void injectKeyManager(MainActivity instance, KeyManager keyManager) {
    instance.keyManager = keyManager;
  }
}
