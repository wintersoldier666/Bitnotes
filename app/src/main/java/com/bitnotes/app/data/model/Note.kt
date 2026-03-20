package com.bitnotes.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Encrypted note entity stored in Room database.
 *
 * ALL text fields are stored encrypted using AES-256-GCM.
 * The plaintext NEVER touches persistent storage.
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** Encrypted note title (Base64-encoded AES-GCM ciphertext) */
    val encryptedTitle: String,

    /** Encrypted note content (Base64-encoded AES-GCM ciphertext) */
    val encryptedContent: String,

    /** Encrypted tags/category (Base64-encoded AES-GCM ciphertext) */
    val encryptedTags: String = "",

    /** Creation timestamp (not sensitive, stored as-is) */
    val createdAt: Long = System.currentTimeMillis(),

    /** Last modified timestamp */
    val updatedAt: Long = System.currentTimeMillis(),

    /** Whether this note is pinned to top */
    val isPinned: Boolean = false,

    /** Color index for note card (0 = default) */
    val colorIndex: Int = 0,

    /** Whether note is in trash */
    val isDeleted: Boolean = false,

    /** When note was moved to trash */
    val deletedAt: Long? = null
)

/**
 * Decrypted note (in-memory only, never persisted).
 */
data class DecryptedNote(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val tags: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false,
    val colorIndex: Int = 0,
    val isDeleted: Boolean = false
)

/**
 * Note preview for list display (decrypted for display).
 */
data class NotePreview(
    val id: Long,
    val title: String,
    val contentPreview: String,
    val tags: String,
    val updatedAt: Long,
    val isPinned: Boolean,
    val colorIndex: Int
)

fun Note.toDecryptedNote(
    title: String,
    content: String,
    tags: String
): DecryptedNote = DecryptedNote(
    id = this.id,
    title = title,
    content = content,
    tags = tags,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    isPinned = this.isPinned,
    colorIndex = this.colorIndex,
    isDeleted = this.isDeleted
)
