package com.bitnotes.app.ui.backup

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitnotes.app.data.model.BackupResult
import com.bitnotes.app.data.model.RestoreResult
import com.bitnotes.app.data.repository.BackupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

sealed class BackupState {
    object Idle : BackupState()
    object Loading : BackupState()
    data class BackupSuccess(val result: BackupResult) : BackupState()
    data class RestoreSuccess(val result: RestoreResult) : BackupState()
    data class Error(val message: String) : BackupState()
}

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val backupRepository: BackupRepository
) : ViewModel() {

    private val _state = MutableLiveData<BackupState>(BackupState.Idle)
    val state: LiveData<BackupState> = _state

    private val _existingBackups = MutableLiveData<List<File>>()
    val existingBackups: LiveData<List<File>> = _existingBackups

    init {
        refreshBackups()
    }

    fun createBackup(password: String) {
        if (password.length < 8) {
            _state.value = BackupState.Error("Backup password must be at least 8 characters")
            return
        }

        viewModelScope.launch {
            _state.value = BackupState.Loading
            val result = backupRepository.createBackup(password)
            _state.value = if (result.success) {
                refreshBackups()
                BackupState.BackupSuccess(result)
            } else {
                BackupState.Error(result.errorMessage ?: "Backup failed")
            }
        }
    }

    fun restoreBackup(uri: Uri, password: String, replaceExisting: Boolean = false) {
        if (password.isEmpty()) {
            _state.value = BackupState.Error("Please enter the backup password")
            return
        }

        viewModelScope.launch {
            _state.value = BackupState.Loading
            val result = backupRepository.restoreBackup(uri, password, replaceExisting)
            _state.value = if (result.success) {
                BackupState.RestoreSuccess(result)
            } else {
                BackupState.Error(result.errorMessage ?: "Restore failed")
            }
        }
    }

    fun deleteBackup(file: File) {
        backupRepository.deleteBackup(file)
        refreshBackups()
    }

    fun getBackupUri(file: File) = backupRepository.getBackupFileUri(file)

    fun refreshBackups() {
        _existingBackups.value = backupRepository.getExistingBackups()
    }

    fun resetState() {
        _state.value = BackupState.Idle
    }
}
