package com.bitnotes.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.bitnotes.app.databinding.ActivityAuthBinding;
import com.bitnotes.app.ui.notes.MainActivity;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.concurrent.Executor;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\b\u0010\u0013\u001a\u00020\u0012H\u0002J\u0012\u0010\u0014\u001a\u00020\u00122\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J\b\u0010\u0017\u001a\u00020\u0012H\u0014J\b\u0010\u0018\u001a\u00020\u0012H\u0002J\b\u0010\u0019\u001a\u00020\u0012H\u0002J\b\u0010\u001a\u001a\u00020\u0012H\u0002J\u0010\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\b\u0010\u001e\u001a\u00020\u0012H\u0002J\u0010\u0010\u001f\u001a\u00020\u00122\u0006\u0010 \u001a\u00020!H\u0002J\b\u0010\"\u001a\u00020\u0012H\u0002J\b\u0010#\u001a\u00020\u0012H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000b\u001a\u00020\f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\r\u0010\u000e\u00a8\u0006$"}, d2 = {"Lcom/bitnotes/app/ui/auth/AuthActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/bitnotes/app/databinding/ActivityAuthBinding;", "executor", "Ljava/util/concurrent/Executor;", "isSetupMode", "", "lockoutTimer", "Landroid/os/CountDownTimer;", "viewModel", "Lcom/bitnotes/app/ui/auth/AuthViewModel;", "getViewModel", "()Lcom/bitnotes/app/ui/auth/AuthViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "handleAction", "", "navigateToMain", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "setupListeners", "setupObservers", "showBiometricPrompt", "showError", "message", "", "showLoading", "showLockout", "remainingMs", "", "showSetupMode", "showUnlockMode", "app_debug"})
public final class AuthActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.bitnotes.app.databinding.ActivityAuthBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private java.util.concurrent.Executor executor;
    private boolean isSetupMode = false;
    @org.jetbrains.annotations.Nullable()
    private android.os.CountDownTimer lockoutTimer;
    
    public AuthActivity() {
        super();
    }
    
    private final com.bitnotes.app.ui.auth.AuthViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupObservers() {
    }
    
    private final void setupListeners() {
    }
    
    private final void handleAction() {
    }
    
    private final void showLoading() {
    }
    
    private final void showSetupMode() {
    }
    
    private final void showUnlockMode() {
    }
    
    private final void showError(java.lang.String message) {
    }
    
    private final void showLockout(long remainingMs) {
    }
    
    private final void navigateToMain() {
    }
    
    private final void showBiometricPrompt() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
}