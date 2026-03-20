package com.bitnotes.app.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.lifecycle.lifecycleScope
import com.bitnotes.app.crypto.KeyManager
import com.bitnotes.app.databinding.ActivitySettingsBinding
import com.bitnotes.app.ui.auth.AuthActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.bitnotes.app.R

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    @Inject
    lateinit var keyManager: KeyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Settings"
        }

        setupUI()
    }

    private fun setupUI() {
        // Biometric toggle
        val biometricManager = BiometricManager.from(this)
        val biometricAvailable = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) == BiometricManager.BIOMETRIC_SUCCESS

        if (!biometricAvailable) {
            binding.switchBiometric.isEnabled = false
            binding.tvBiometricHint.text = "Biometric hardware not available on this device"
        }

        // Load state first, THEN attach listener to avoid triggering on initial set
        lifecycleScope.launch {
            val biometricEnabled = keyManager.isBiometricEnabled()
            binding.switchBiometric.isChecked = biometricEnabled

            // Attach listener only after initial state is set
            binding.switchBiometric.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    showEnableBiometricDialog()
                } else {
                    lifecycleScope.launch {
                        keyManager.disableBiometric()
                        Snackbar.make(binding.root, "Biometric unlock disabled", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Change PIN
        binding.btnChangePin.setOnClickListener {
            showChangePinDialog()
        }

        // Security info
        binding.btnSecurityInfo.setOnClickListener {
            showSecurityInfo()
        }

        // Wipe all data
        binding.btnWipeAll.setOnClickListener {
            showWipeConfirmation()
        }
    }

    private fun showEnableBiometricDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_restore_password, null)
        val passwordField = dialogView.findViewById<TextInputEditText>(R.id.etPassword)

        MaterialAlertDialogBuilder(this)
            .setTitle("Enable Biometric Unlock")
            .setMessage("Enter your current PIN to enable biometric unlock.")
            .setView(dialogView)
            .setPositiveButton("Enable") { _, _ ->
                val pin = passwordField.text?.toString() ?: ""
                lifecycleScope.launch {
                    try {
                        keyManager.enableBiometric(pin)
                        Snackbar.make(
                            binding.root,
                            "Biometric unlock enabled",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        binding.switchBiometric.isChecked = false
                        Snackbar.make(
                            binding.root,
                            "Failed: ${e.message}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Cancel") { _, _ ->
                binding.switchBiometric.isChecked = false
            }
            .show()
    }

    private fun showChangePinDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_pin, null)
        val currentPinField = dialogView.findViewById<TextInputEditText>(R.id.etCurrentPin)
        val newPinField = dialogView.findViewById<TextInputEditText>(R.id.etNewPin)
        val confirmPinField = dialogView.findViewById<TextInputEditText>(R.id.etConfirmPin)

        MaterialAlertDialogBuilder(this)
            .setTitle("Change PIN")
            .setView(dialogView)
            .setPositiveButton("Change") { _, _ ->
                val currentPin = currentPinField.text?.toString() ?: ""
                val newPin = newPinField.text?.toString() ?: ""
                val confirmPin = confirmPinField.text?.toString() ?: ""

                if (newPin != confirmPin) {
                    Snackbar.make(binding.root, "New PINs don't match", Snackbar.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (newPin.length < 6) {
                    Snackbar.make(binding.root, "PIN must be at least 6 digits", Snackbar.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                lifecycleScope.launch {
                    val success = keyManager.changePin(currentPin, newPin)
                    if (success) {
                        Snackbar.make(binding.root, "PIN changed successfully", Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(binding.root, "Wrong current PIN", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showSecurityInfo() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Security Information")
            .setMessage(
                "Bitnotes Security Features:\n\n" +
                "• AES-256-GCM encryption\n" +
                "• PBKDF2-SHA256 key derivation\n  (310,000 iterations)\n" +
                "• Android Keystore hardware-backed keys\n" +
                "• Zero network access (no INTERNET permission)\n" +
                "• Screenshot prevention (FLAG_SECURE)\n" +
                "• No cloud backup of encrypted data\n" +
                "• Brute-force lockout (5 attempts)\n" +
                "• Unique IV per encryption operation\n" +
                "• Separate backup encryption password\n\n" +
                "Your notes never leave your device unencrypted."
            )
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showWipeConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle("⚠ Wipe All Data")
            .setMessage(
                "This will permanently delete ALL notes and encryption keys.\n\n" +
                "This action CANNOT be undone.\n\n" +
                "Make sure you have a backup before proceeding."
            )
            .setPositiveButton("WIPE EVERYTHING") { _, _ ->
                showFinalWipeConfirmation()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showFinalWipeConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Are you absolutely sure?")
            .setMessage("Type your PIN to confirm permanent data deletion.")
            .setPositiveButton("Confirm Wipe") { _, _ ->
                lifecycleScope.launch {
                    // Delete all Room database records
                    keyManager.wipeAll()
                    val intent = Intent(this@SettingsActivity, AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
