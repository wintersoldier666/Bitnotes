package com.bitnotes.app.di;

import android.content.Context;
import com.bitnotes.app.data.db.DatabaseManager;
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
public final class AppModule_ProvideDatabaseManagerFactory implements Factory<DatabaseManager> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideDatabaseManagerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public DatabaseManager get() {
    return provideDatabaseManager(contextProvider.get());
  }

  public static AppModule_ProvideDatabaseManagerFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideDatabaseManagerFactory(contextProvider);
  }

  public static DatabaseManager provideDatabaseManager(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideDatabaseManager(context));
  }
}
