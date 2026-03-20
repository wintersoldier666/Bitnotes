package com.bitnotes.app.ui.settings;

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
public final class SettingsActivity_MembersInjector implements MembersInjector<SettingsActivity> {
  private final Provider<KeyManager> keyManagerProvider;

  public SettingsActivity_MembersInjector(Provider<KeyManager> keyManagerProvider) {
    this.keyManagerProvider = keyManagerProvider;
  }

  public static MembersInjector<SettingsActivity> create(Provider<KeyManager> keyManagerProvider) {
    return new SettingsActivity_MembersInjector(keyManagerProvider);
  }

  @Override
  public void injectMembers(SettingsActivity instance) {
    injectKeyManager(instance, keyManagerProvider.get());
  }

  @InjectedFieldSignature("com.bitnotes.app.ui.settings.SettingsActivity.keyManager")
  public static void injectKeyManager(SettingsActivity instance, KeyManager keyManager) {
    instance.keyManager = keyManager;
  }
}
