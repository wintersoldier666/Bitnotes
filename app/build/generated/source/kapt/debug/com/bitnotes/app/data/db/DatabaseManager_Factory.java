package com.bitnotes.app.data.db;

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
public final class DatabaseManager_Factory implements Factory<DatabaseManager> {
  private final Provider<Context> contextProvider;

  public DatabaseManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public DatabaseManager get() {
    return newInstance(contextProvider.get());
  }

  public static DatabaseManager_Factory create(Provider<Context> contextProvider) {
    return new DatabaseManager_Factory(contextProvider);
  }

  public static DatabaseManager newInstance(Context context) {
    return new DatabaseManager(context);
  }
}
