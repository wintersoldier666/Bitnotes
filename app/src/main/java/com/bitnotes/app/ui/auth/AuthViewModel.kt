package com.bitnotes.app.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitnotes.app.crypto.KeyManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Loading : AuthState()
    object NeedsSetup : AuthState()
    object Locked : AuthState()
    object Unlocked : AuthState()
    data class Error(val message: String) : AuthState()
    data class LockedOut(val remainingMs: Long) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val keyManager: KeyManager
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Loading)
    val authState: LiveData<AuthState> = _authState

    private val _failedAttempts = MutableLiveData(0)
    val failedAttempts: LiveData<Int> = _failedAttempts

    private val _biometricEnabled = MutableLiveData(false)
    val biometricEnabled: LiveData<Boolean> = _biometricEnabled

    init {
        checkInitialState()
    }

    private fun checkInitialState() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when {
                !keyManager.isSetup() -> _authState.value = AuthState.NeedsSetup
                keyManager.isLockedOut() -> {
                    val remaining = keyManager.getLockoutRemainingMs()
                    _authState.value = AuthState.LockedOut(remaining)
                }
                else -> {
                    _biometricEnabled.value = keyManager.isBiometricEnabled()
                    _failedAttempts.value = keyManager.getFailedAttempts()
                    _authState.value = AuthState.Locked
                }
            }
        }
    }

    fun setupPin(pin: String, confirmPin: String) {
        if (pin != confirmPin) {
            _authState.value = AuthState.Error("PINs do not match")
            return
        }
        if (pin.length < 6) {
            _authState.value = AuthState.Error("PIN must be at least 6 digits")
            return
        }
        viewModelScope.launch {
            try {
                keyManager.setupWithPin(pin)
                _authState.value = AuthState.Unlocked
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Setup failed: ${e.message}")
            }
        }
    }

    fun unlockWithPin(pin: String) {
        viewModelScope.launch {
            if (keyManager.isLockedOut()) {
                val remaining = keyManager.getLockoutRemainingMs()
                _authState.value = AuthState.LockedOut(remaining)
                return@launch
            }

            val success = keyManager.unlockWithPin(pin)
            if (success) {
                _authState.value = AuthState.Unlocked
            } else {
                val attempts = keyManager.getFailedAttempts()
                _failedAttempts.value = attempts
                if (keyManager.isLockedOut()) {
                    val remaining = keyManager.getLockoutRemainingMs()
                    _authState.value = AuthState.LockedOut(remaining)
                } else {
                    val remaining = KeyManager.MAX_ATTEMPTS - attempts
                    _authState.value = AuthState.Error(
                        "Wrong PIN. $remaining attempt${if (remaining == 1) "" else "s"} remaining"
                    )
                }
            }
        }
    }

    fun unlockWithBiometric() {
        viewModelScope.launch {
            val success = keyManager.unlockWithBiometric()
            if (success) {
                _authState.value = AuthState.Unlocked
            } else {
                _authState.value = AuthState.Error("Biometric authentication failed")
            }
        }
    }

    fun resetState() {
        checkInitialState()
    }
}
