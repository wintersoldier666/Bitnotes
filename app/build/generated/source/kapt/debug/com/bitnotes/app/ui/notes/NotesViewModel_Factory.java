package com.bitnotes.app.ui.notes;

import com.bitnotes.app.data.repository.NoteRepository;
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
public final class NotesViewModel_Factory implements Factory<NotesViewModel> {
  private final Provider<NoteRepository> noteRepositoryProvider;

  public NotesViewModel_Factory(Provider<NoteRepository> noteRepositoryProvider) {
    this.noteRepositoryProvider = noteRepositoryProvider;
  }

  @Override
  public NotesViewModel get() {
    return newInstance(noteRepositoryProvider.get());
  }

  public static NotesViewModel_Factory create(Provider<NoteRepository> noteRepositoryProvider) {
    return new NotesViewModel_Factory(noteRepositoryProvider);
  }

  public static NotesViewModel newInstance(NoteRepository noteRepository) {
    return new NotesViewModel(noteRepository);
  }
}
