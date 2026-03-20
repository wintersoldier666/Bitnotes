package com.bitnotes.app.ui.backup;

import com.bitnotes.app.data.repository.BackupRepository;
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
public final class BackupViewModel_Factory implements Factory<BackupViewModel> {
  private final Provider<BackupRepository> backupRepositoryProvider;

  public BackupViewModel_Factory(Provider<BackupRepository> backupRepositoryProvider) {
    this.backupRepositoryProvider = backupRepositoryProvider;
  }

  @Override
  public BackupViewModel get() {
    return newInstance(backupRepositoryProvider.get());
  }

  public static BackupViewModel_Factory create(
      Provider<BackupRepository> backupRepositoryProvider) {
    return new BackupViewModel_Factory(backupRepositoryProvider);
  }

  public static BackupViewModel newInstance(BackupRepository backupRepository) {
    return new BackupViewModel(backupRepository);
  }
}
