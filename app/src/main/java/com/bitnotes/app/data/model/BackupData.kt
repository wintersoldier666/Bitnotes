package com.bitnotes.app.data.model

/**
 * Data classes for encrypted backup/restore.
 * The backup file itself is AES-256-GCM encrypted with a user-provided password.
 */
data class BackupData(
    val version: Int = 1,
    val appVersion: String = "1.0.0",
    val exportedAt: Long = System.currentTimeMillis(),
    val noteCount: Int = 0,
    val notes: List<NoteBackup> = emptyList()
)

data class NoteBackup(
    val id: Long,
    val title: String,
    val content: String,
    val tags: String,
    val createdAt: Long,
    val updatedAt: Long,
    val isPinned: Boolean,
    val colorIndex: Int
)

data class BackupResult(
    val success: Boolean,
    val filePath: String? = null,
    val noteCount: Int = 0,
    val errorMessage: String? = null
)

data class RestoreResult(
    val success: Boolean,
    val noteCount: Int = 0,
    val errorMessage: String? = null
)
