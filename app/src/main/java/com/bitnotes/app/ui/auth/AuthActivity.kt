package com.bitnotes.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.bitnotes.app.databinding.ActivityAuthBinding
import com.bitnotes.app.ui.notes.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var executor: Executor
    private var isSetupMode = false
    private var lockoutTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executor = ContextCompat.getMainExecutor(this)
        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> showLoading()
                is AuthState.NeedsSetup -> showSetupMode()
                is AuthState.Locked -> showUnlockMode()
                is AuthState.Unlocked -> navigateToMain()
                is AuthState.Error -> showError(state.message)
                is AuthState.LockedOut -> showLockout(state.remainingMs)
            }
        }

        viewModel.biometricEnabled.observe(this) { enabled ->
            binding.btnBiometric.visibility = if (enabled && !isSetupMode) View.VISIBLE else View.GONE
        }

        viewModel.failedAttempts.observe(this) { attempts ->
            if (attempts > 0 && !isSetupMode) {
                binding.tvAttemptsWarning.text = "Failed attempts: $attempts / ${
                    com.bitnotes.app.crypto.KeyManager.MAX_ATTEMPTS
                }"
                binding.tvAttemptsWarning.visibility = View.VISIBLE
            } else {
                binding.tvAttemptsWarning.visibility = View.GONE
            }
        }
    }

    private fun setupListeners() {
        binding.btnAction.setOnClickListener { handleAction() }

        binding.etPin.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleAction()
                true
            } else false
        }

        binding.etPin.doAfterTextChanged {
            binding.tilPin.error = null
        }

        binding.etConfirmPin.doAfterTextChanged {
            binding.tilConfirmPin.error = null
        }

        binding.btnBiometric.setOnClickListener {
            showBiometricPrompt()
        }
    }

    private fun handleAction() {
        val pin = binding.etPin.text?.toString() ?: ""
        if (isSetupMode) {
            val confirmPin = binding.etConfirmPin.text?.toString() ?: ""
            viewModel.setupPin(pin, confirmPin)
        } else {
            viewModel.unlockWithPin(pin)
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.contentGroup.visibility = View.GONE
    }

    private fun showSetupMode() {
        isSetupMode = true
        binding.progressBar.visibility = View.GONE
        binding.contentGroup.visibility = View.VISIBLE
        binding.tvTitle.text = "Create PIN"
        binding.tvSubtitle.text = "Set a secure PIN to protect your notes.\nMinimum 6 digits."
        binding.tilConfirmPin.visibility = View.VISIBLE
        binding.btnAction.text = "Create PIN"
        binding.btnBiometric.visibility = View.GONE
        binding.tvAttemptsWarning.visibility = View.GONE
        binding.tilPin.hint = "Enter PIN (min 6 digits)"
        binding.tilConfirmPin.hint = "Confirm PIN"
    }

    private fun showUnlockMode() {
        isSetupMode = false
        binding.progressBar.visibility = View.GONE
        binding.contentGroup.visibility = View.VISIBLE
        binding.tvTitle.text = "Bitnotes"
        binding.lockoutGroup.visibility = View.GONE
        binding.btnAction.isEnabled = true
        binding.etPin.isEnabled = true
        binding.tilConfirmPin.visibility = View.GONE
        binding.btnAction.text = "Unlock"
        binding.tilPin.hint = "PIN"

        // Auto-launch biometric prompt if enabled
        val biometricEnabled = viewModel.biometricEnabled.value ?: false
        if (biometricEnabled) {
            binding.tvSubtitle.text = "Use biometric or enter your PIN"
            // Slight delay so the UI is visible before the prompt appears
            binding.root.postDelayed({ showBiometricPrompt() }, 300)
        } else {
            binding.tvSubtitle.text = "Enter your PIN to unlock"
        }
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.tilPin.error = message
        binding.etPin.text?.clear()
        if (isSetupMode) {
            binding.etConfirmPin.text?.clear()
        }
    }

    private fun showLockout(remainingMs: Long) {
        binding.progressBar.visibility = View.GONE
        binding.lockoutGroup.visibility = View.VISIBLE
        binding.btnAction.isEnabled = false
        binding.etPin.isEnabled = false
        binding.btnBiometric.isEnabled = false

        lockoutTimer?.cancel()
        lockoutTimer = object : CountDownTimer(remainingMs, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                binding.tvLockoutMessage.text =
                    "Too many failed attempts. Try again in ${seconds}s"
            }

            override fun onFinish() {
                binding.lockoutGroup.visibility = View.GONE
                binding.btnAction.isEnabled = true
                binding.etPin.isEnabled = true
                binding.btnBiometric.isEnabled = true
                binding.tvAttemptsWarning.visibility = View.GONE
                viewModel.resetState()
            }
        }.start()
    }

    private fun navigateToMain() {
        lockoutTimer?.cancel()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showBiometricPrompt() {
        val biometricManager = BiometricManager.from(this)
        val canAuthenticate = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        )

        if (canAuthenticate != BiometricManager.BIOMETRIC_SUCCESS) {
            Toast.makeText(this, "Biometric not available", Toast.LENGTH_SHORT).show()
            return
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Unlock")
            .setSubtitle("Use your fingerprint or face to unlock Bitnotes")
            .setNegativeButtonText("Use PIN instead")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        val biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    viewModel.unlockWithBiometric()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    if (errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON &&
                        errorCode != BiometricPrompt.ERROR_USER_CANCELED
                    ) {
                        Toast.makeText(
                            this@AuthActivity,
                            "Auth error: $errString",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(
                        this@AuthActivity,
                        "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        biometricPrompt.authenticate(promptInfo)
    }

    override fun onDestroy() {
        super.onDestroy()
        lockoutTimer?.cancel()
    }
}
