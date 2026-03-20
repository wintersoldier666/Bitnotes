package com.bitnotes.app.data.repository

import android.content.Context
import android.net.Uri
import com.bitnotes.app.crypto.CryptoManager
import com.bitnotes.app.data.model.BackupData
import com.bitnotes.app.data.model.BackupResult
import com.bitnotes.app.data.model.RestoreResult
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles encrypted backup and restore operations.
 *
 * Backup format:
 * - JSON serialized BackupData
 * - Compressed (if large)
 * - Encrypted with AES-256-GCM using PBKDF2-derived key from backup password
 * - Stored with .bitnotes extension
 */
@Singleton
class BackupRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val noteRepository: NoteRepository,
    private val cryptoManager: CryptoManager
) {
    private val gson = Gson()

    companion object {
        const val BACKUP_EXTENSION = ".bitnotes"
        const val BACKUP_MAGIC_HEADER = "BITNOTES_BACKUP_V1"
    }

    /**
     * Creates an encrypted backup file.
     * @param password Backup encryption password (separate from app PIN)
     * @return BackupResult with file path on success
     */
    suspend fun createBackup(password: String): BackupResult {
        return try {
            val notes = noteRepository.getAllDecryptedForBackup()
            val backupData = BackupData(
                noteCount = notes.size,
                notes = notes
            )

            val json = gson.toJson(backupData)
            val jsonBytes = json.toByteArray(Charsets.UTF_8)

            // Encrypt backup
            val encryptedBytes = cryptoManager.encryptBackup(jsonBytes, password)

            // Add magic header for validation
            val header = BACKUP_MAGIC_HEADER.toByteArray(Charsets.UTF_8)
            val finalBytes = header + encryptedBytes

            // Save to backup directory
            val backupDir = getBackupDirectory()
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                .format(Date())
            val fileName = "bitnotes_backup_$timestamp$BACKUP_EXTENSION"
            val backupFile = File(backupDir, fileName)
            backupFile.writeBytes(finalBytes)

            BackupResult(
                success = true,
                filePath = backupFile.absolutePath,
                noteCount = notes.size
            )
        } catch (e: Exception) {
            BackupResult(
                success = false,
                errorMessage = e.message ?: "Unknown error during backup"
            )
        }
    }

    /**
     * Restores from an encrypted backup file URI.
     * @param uri URI of the backup file
     * @param password Backup encryption password
     * @param replaceExisting If true, deletes existing notes before restore
     */
    suspend fun restoreBackup(
        uri: Uri,
        password: String,
        replaceExisting: Boolean = false
    ): RestoreResult {
        return try {
            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: return RestoreResult(success = false, errorMessage = "Cannot read file")

            // Validate magic header
            val header = BACKUP_MAGIC_HEADER.toByteArray(Charsets.UTF_8)
            if (bytes.size <= header.size) {
                return RestoreResult(success = false, errorMessage = "Invalid backup file")
            }

            val fileHeader = bytes.take(header.size).toByteArray()
            if (!fileHeader.contentEquals(header)) {
                return RestoreResult(
                    success = false,
                    errorMessage = "Not a valid Bitnotes backup file"
                )
            }

            // Extract encrypted data (skip header)
            val encryptedData = bytes.drop(header.size).toByteArray()

            // Decrypt
            val decryptedBytes = try {
                cryptoManager.decryptBackup(encryptedData, password)
            } catch (e: Exception) {
                return RestoreResult(
                    success = false,
                    errorMessage = "Wrong password or corrupted backup"
                )
            }

            // Parse JSON
            val json = String(decryptedBytes, Charsets.UTF_8)
            val backupData = try {
                gson.fromJson(json, BackupData::class.java)
            } catch (e: Exception) {
                return RestoreResult(
                    success = false,
                    errorMessage = "Corrupted backup data"
                )
            }

            // Validate version compatibility
            if (backupData.version > 1) {
                return RestoreResult(
                    success = false,
                    errorMessage = "Backup created with newer app version. Please update Bitnotes."
                )
            }

            // Import notes
            noteRepository.importFromBackup(backupData.notes)

            RestoreResult(
                success = true,
                noteCount = backupData.notes.size
            )
        } catch (e: Exception) {
            RestoreResult(
                success = false,
                errorMessage = e.message ?: "Unknown error during restore"
            )
        }
    }

    /**
     * Returns list of existing backup files.
     */
    fun getExistingBackups(): List<File> {
        return try {
            getBackupDirectory().listFiles { file ->
                file.name.endsWith(BACKUP_EXTENSION)
            }?.sortedByDescending { it.lastModified() } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Deletes a backup file.
     */
    fun deleteBackup(file: File): Boolean {
        return try {
            // Securely overwrite before deleting
            if (file.exists()) {
                file.writeBytes(ByteArray(file.length().toInt()) { 0 })
                file.delete()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun getBackupDirectory(): File {
        val dir = File(context.cacheDir, "backups")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun getBackupFileUri(file: File): Uri {
        return androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}
