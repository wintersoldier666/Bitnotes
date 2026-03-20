package com.bitnotes.app.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import com.bitnotes.app.crypto.KeyManager;
import com.bitnotes.app.databinding.ActivitySettingsBinding;
import com.bitnotes.app.ui.auth.AuthActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;
import com.bitnotes.app.R;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0014J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\fH\u0002J\b\u0010\u0014\u001a\u00020\fH\u0002J\b\u0010\u0015\u001a\u00020\fH\u0002J\b\u0010\u0016\u001a\u00020\fH\u0002J\b\u0010\u0017\u001a\u00020\fH\u0002J\b\u0010\u0018\u001a\u00020\fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n\u00a8\u0006\u0019"}, d2 = {"Lcom/bitnotes/app/ui/settings/SettingsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/bitnotes/app/databinding/ActivitySettingsBinding;", "keyManager", "Lcom/bitnotes/app/crypto/KeyManager;", "getKeyManager", "()Lcom/bitnotes/app/crypto/KeyManager;", "setKeyManager", "(Lcom/bitnotes/app/crypto/KeyManager;)V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "setupUI", "showChangePinDialog", "showEnableBiometricDialog", "showFinalWipeConfirmation", "showSecurityInfo", "showWipeConfirmation", "app_debug"})
public final class SettingsActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.bitnotes.app.databinding.ActivitySettingsBinding binding;
    @javax.inject.Inject()
    public com.bitnotes.app.crypto.KeyManager keyManager;
    
    public SettingsActivity() {
        super();
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
    
    private final void setupUI() {
    }
    
    private final void showEnableBiometricDialog() {
    }
    
    private final void showChangePinDialog() {
    }
    
    private final void showSecurityInfo() {
    }
    
    private final void showWipeConfirmation() {
    }
    
    private final void showFinalWipeConfirmation() {
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
}