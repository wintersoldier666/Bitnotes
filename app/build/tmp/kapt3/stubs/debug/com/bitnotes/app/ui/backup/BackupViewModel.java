package com.bitnotes.app.ui.backup;

import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.bitnotes.app.data.model.BackupResult;
import com.bitnotes.app.data.model.RestoreResult;
import com.bitnotes.app.data.repository.BackupRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import java.io.File;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\bJ\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0016\u001a\u00020\bJ\u0006\u0010\u0019\u001a\u00020\u0012J\u0006\u0010\u001a\u001a\u00020\u0012J \u0010\u001b\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\u00182\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u001d\u001a\u00020\u001eR\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000e\u00a8\u0006\u001f"}, d2 = {"Lcom/bitnotes/app/ui/backup/BackupViewModel;", "Landroidx/lifecycle/ViewModel;", "backupRepository", "Lcom/bitnotes/app/data/repository/BackupRepository;", "(Lcom/bitnotes/app/data/repository/BackupRepository;)V", "_existingBackups", "Landroidx/lifecycle/MutableLiveData;", "", "Ljava/io/File;", "_state", "Lcom/bitnotes/app/ui/backup/BackupState;", "existingBackups", "Landroidx/lifecycle/LiveData;", "getExistingBackups", "()Landroidx/lifecycle/LiveData;", "state", "getState", "createBackup", "", "password", "", "deleteBackup", "file", "getBackupUri", "Landroid/net/Uri;", "refreshBackups", "resetState", "restoreBackup", "uri", "replaceExisting", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class BackupViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.bitnotes.app.data.repository.BackupRepository backupRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.bitnotes.app.ui.backup.BackupState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.bitnotes.app.ui.backup.BackupState> state = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<java.io.File>> _existingBackups = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<java.io.File>> existingBackups = null;
    
    @javax.inject.Inject()
    public BackupViewModel(@org.jetbrains.annotations.NotNull()
    com.bitnotes.app.data.repository.BackupRepository backupRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.bitnotes.app.ui.backup.BackupState> getState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<java.io.File>> getExistingBackups() {
        return null;
    }
    
    public final void createBackup(@org.jetbrains.annotations.NotNull()
    java.lang.String password) {
    }
    
    public final void restoreBackup(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    java.lang.String password, boolean replaceExisting) {
    }
    
    public final void deleteBackup(@org.jetbrains.annotations.NotNull()
    java.io.File file) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.net.Uri getBackupUri(@org.jetbrains.annotations.NotNull()
    java.io.File file) {
        return null;
    }
    
    public final void refreshBackups() {
    }
    
    public final void resetState() {
    }
}