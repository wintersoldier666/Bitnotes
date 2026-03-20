package com.bitnotes.app.ui.notes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.bitnotes.app.data.model.DecryptedNote;
import com.bitnotes.app.data.repository.NoteRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\rJ \u0010\u0015\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00172\b\b\u0002\u0010\u0019\u001a\u00020\u0017J\u000e\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\tR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/bitnotes/app/ui/notes/NoteEditorViewModel;", "Landroidx/lifecycle/ViewModel;", "noteRepository", "Lcom/bitnotes/app/data/repository/NoteRepository;", "(Lcom/bitnotes/app/data/repository/NoteRepository;)V", "_editorState", "Landroidx/lifecycle/MutableLiveData;", "Lcom/bitnotes/app/ui/notes/EditorState;", "currentColorIndex", "", "currentIsPinned", "", "currentNoteId", "", "editorState", "Landroidx/lifecycle/LiveData;", "getEditorState", "()Landroidx/lifecycle/LiveData;", "loadNote", "", "noteId", "saveNote", "title", "", "content", "tags", "setColorIndex", "index", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class NoteEditorViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.bitnotes.app.data.repository.NoteRepository noteRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.bitnotes.app.ui.notes.EditorState> _editorState = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.bitnotes.app.ui.notes.EditorState> editorState = null;
    private long currentNoteId = -1L;
    private int currentColorIndex = 0;
    private boolean currentIsPinned = false;
    
    @javax.inject.Inject()
    public NoteEditorViewModel(@org.jetbrains.annotations.NotNull()
    com.bitnotes.app.data.repository.NoteRepository noteRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.bitnotes.app.ui.notes.EditorState> getEditorState() {
        return null;
    }
    
    public final void loadNote(long noteId) {
    }
    
    public final void saveNote(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String content, @org.jetbrains.annotations.NotNull()
    java.lang.String tags) {
    }
    
    public final void setColorIndex(int index) {
    }
}