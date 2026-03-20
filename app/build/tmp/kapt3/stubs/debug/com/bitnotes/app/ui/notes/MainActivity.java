package com.bitnotes.app.ui.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.bitnotes.app.R;
import com.bitnotes.app.crypto.KeyManager;
import com.bitnotes.app.data.model.NotePreview;
import com.bitnotes.app.databinding.ActivityMainBinding;
import com.bitnotes.app.ui.auth.AuthActivity;
import com.bitnotes.app.ui.backup.BackupActivity;
import com.bitnotes.app.ui.settings.SettingsActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0012\u0010\u0017\u001a\u00020\u00142\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0014J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0010\u0010\u001e\u001a\u00020\u001b2\u0006\u0010\u001f\u001a\u00020 H\u0016J\b\u0010!\u001a\u00020\u0014H\u0014J\u0010\u0010\"\u001a\u00020\u00142\u0006\u0010#\u001a\u00020$H\u0002J\b\u0010%\u001a\u00020\u0014H\u0002J\b\u0010&\u001a\u00020\u0014H\u0002J\b\u0010\'\u001a\u00020\u0014H\u0002J\b\u0010(\u001a\u00020\u0014H\u0002J\u0010\u0010)\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\r\u001a\u00020\u000e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006*"}, d2 = {"Lcom/bitnotes/app/ui/notes/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/bitnotes/app/databinding/ActivityMainBinding;", "keyManager", "Lcom/bitnotes/app/crypto/KeyManager;", "getKeyManager", "()Lcom/bitnotes/app/crypto/KeyManager;", "setKeyManager", "(Lcom/bitnotes/app/crypto/KeyManager;)V", "notesAdapter", "Lcom/bitnotes/app/ui/notes/NotesAdapter;", "viewModel", "Lcom/bitnotes/app/ui/notes/NotesViewModel;", "getViewModel", "()Lcom/bitnotes/app/ui/notes/NotesViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "confirmDelete", "", "note", "Lcom/bitnotes/app/data/model/NotePreview;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "", "menu", "Landroid/view/Menu;", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "onResume", "openNoteEditor", "noteId", "", "setupBackPress", "setupFab", "setupObservers", "setupRecyclerView", "showNoteOptions", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.bitnotes.app.databinding.ActivityMainBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.bitnotes.app.ui.notes.NotesAdapter notesAdapter;
    @javax.inject.Inject()
    public com.bitnotes.app.crypto.KeyManager keyManager;
    
    public MainActivity() {
        super();
    }
    
    private final com.bitnotes.app.ui.notes.NotesViewModel getViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bitnotes.app.crypto.KeyManager getKeyManager() {
        return null;
    }
    
    public final void setKeyManager(@org.jetbrains.annotations.NotNull()
    com.bitnotes.app.crypto.KeyManager p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void setupFab() {
    }
    
    private final void setupObservers() {
    }
    
    private final void setupBackPress() {
    }
    
    private final void openNoteEditor(long noteId) {
    }
    
    private final void showNoteOptions(com.bitnotes.app.data.model.NotePreview note) {
    }
    
    private final void confirmDelete(com.bitnotes.app.data.model.NotePreview note) {
    }
    
    @java.lang.Override()
    public boolean onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu) {
        return false;
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
}