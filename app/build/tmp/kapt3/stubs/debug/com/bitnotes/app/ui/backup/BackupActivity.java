package com.bitnotes.app.ui.backup;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.bitnotes.app.R;
import com.bitnotes.app.databinding.ActivityBackupBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import dagger.hilt.android.AndroidEntryPoint;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0012\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u0014H\u0002J\b\u0010\u001c\u001a\u00020\u0014H\u0002J\b\u0010\u001d\u001a\u00020\u0014H\u0002J\b\u0010\u001e\u001a\u00020\u0014H\u0002J\u0010\u0010\u001f\u001a\u00020\u00142\u0006\u0010 \u001a\u00020!H\u0002J\u0018\u0010\"\u001a\u00020\u00142\u0006\u0010#\u001a\u00020\u00102\u0006\u0010$\u001a\u00020%H\u0002J\u0016\u0010&\u001a\u00020\u00142\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020)0(H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0005\u001a\u0010\u0012\f\u0012\n \b*\u0004\u0018\u00010\u00070\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\f\u00a8\u0006*"}, d2 = {"Lcom/bitnotes/app/ui/backup/BackupActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/bitnotes/app/databinding/ActivityBackupBinding;", "pickFileLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/content/Intent;", "kotlin.jvm.PlatformType", "viewModel", "Lcom/bitnotes/app/ui/backup/BackupViewModel;", "getViewModel", "()Lcom/bitnotes/app/ui/backup/BackupViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "formatFileSize", "", "bytes", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "pickBackupFile", "setupButtons", "setupObservers", "showBackupPasswordDialog", "showRestorePasswordDialog", "uri", "Landroid/net/Uri;", "showShareDialog", "filePath", "noteCount", "", "updateBackupsList", "backups", "", "Ljava/io/File;", "app_debug"})
public final class BackupActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.bitnotes.app.databinding.ActivityBackupBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<android.content.Intent> pickFileLauncher = null;
    
    public BackupActivity() {
        super();
    }
    
    private final com.bitnotes.app.ui.backup.BackupViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupButtons() {
    }
    
    private final void setupObservers() {
    }
    
    private final void showBackupPasswordDialog() {
    }
    
    private final void showRestorePasswordDialog(android.net.Uri uri) {
    }
    
    private final void showShareDialog(java.lang.String filePath, int noteCount) {
    }
    
    private final void pickBackupFile() {
    }
    
    private final void updateBackupsList(java.util.List<? extends java.io.File> backups) {
    }
    
    private final java.lang.String formatFileSize(long bytes) {
        return null;
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
}