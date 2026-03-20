package com.bitnotes.app.data.repository

import com.bitnotes.app.crypto.CryptoManager
import com.bitnotes.app.crypto.KeyManager
import com.bitnotes.app.data.db.DatabaseManager
import com.bitnotes.app.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles encrypt/decrypt operations transparently.
 * Notes are always encrypted before storage and decrypted on retrieval.
 */
@Singleton
class NoteRepository @Inject constructor(
    private val databaseManager: DatabaseManager,
    private val cryptoManager: CryptoManager,
    private val keyManager: KeyManager
) {
    private val dao get() = databaseManager.getDatabase().noteDao()

    // ───────────── Read Operations ─────────────

    /**
     * Returns a flow of decrypted note previews for the list screen.
     */
    fun getAllNotePreviews(): Flow<List<NotePreview>> {
        return dao.getAllNotes().map { notes ->
            val dek = keyManager.getDataEncryptionKey() ?: return@map emptyList()
            notes.mapNotNull { note ->
                try {
                    val title = cryptoManager.decryptString(note.encryptedTitle, dek)
                    val content = cryptoManager.decryptString(note.encryptedContent, dek)
                    val tags = if (note.encryptedTags.isNotEmpty()) {
                        cryptoManager.decryptString(note.encryptedTags, dek)
                    } else ""

                    NotePreview(
                        id = note.id,
                        title = title,
                        contentPreview = content.take(120),
                        tags = tags,
                        updatedAt = note.updatedAt,
                        isPinned = note.isPinned,
                        colorIndex = note.colorIndex
                    )
                } catch (e: Exception) {
                    // Skip corrupted/undecryptable notes
                    null
                }
            }
        }
    }

    /**
     * Returns a flow of decrypted notes in trash.
     */
    fun getTrashPreviews(): Flow<List<NotePreview>> {
        return dao.getDeletedNotes().map { notes ->
            val dek = keyManager.getDataEncryptionKey() ?: return@map emptyList()
            notes.mapNotNull { note ->
                try {
                    val title = cryptoManager.decryptString(note.encryptedTitle, dek)
                    val content = cryptoManager.decryptString(note.encryptedContent, dek)
                    val tags = if (note.encryptedTags.isNotEmpty()) {
                        cryptoManager.decryptString(note.encryptedTags, dek)
                    } else ""

                    NotePreview(
                        id = note.id,
                        title = title,
                        contentPreview = content.take(120),
                        tags = tags,
                        updatedAt = note.updatedAt,
                        isPinned = note.isPinned,
                        colorIndex = note.colorIndex
                    )
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    /**
     * Gets a single decrypted note by ID.
     */
    suspend fun getNoteById(id: Long): DecryptedNote? {
        val note = dao.getNoteById(id) ?: return null
        val dek = keyManager.getDataEncryptionKey() ?: return null

        return try {
            val title = cryptoManager.decryptString(note.encryptedTitle, dek)
            val content = cryptoManager.decryptString(note.encryptedContent, dek)
            val tags = if (note.encryptedTags.isNotEmpty()) {
                cryptoManager.decryptString(note.encryptedTags, dek)
            } else ""

            note.toDecryptedNote(title, content, tags)
        } catch (e: Exception) {
            null
        }
    }

    // ───────────── Write Operations ─────────────

    /**
     * Creates a new encrypted note.
     */
    suspend fun createNote(
        title: String,
        content: String,
        tags: String = "",
        colorIndex: Int = 0
    ): Long {
        val dek = keyManager.getDataEncryptionKey()
            ?: throw IllegalStateException("App is locked")

        val now = System.currentTimeMillis()
        val note = com.bitnotes.app.data.model.Note(
            encryptedTitle = cryptoManager.encryptString(
                title.ifEmpty { "Untitled" },
                dek
            ),
            encryptedContent = cryptoManager.encryptString(content, dek),
            encryptedTags = if (tags.isNotEmpty()) cryptoManager.encryptString(tags, dek) else "",
            createdAt = now,
            updatedAt = now,
            colorIndex = colorIndex
        )
        return dao.insertNote(note)
    }

    /**
     * Updates an existing note with new encrypted content.
     */
    suspend fun updateNote(
        id: Long,
        title: String,
        content: String,
        tags: String = "",
        colorIndex: Int = 0,
        isPinned: Boolean = false
    ) {
        val dek = keyManager.getDataEncryptionKey()
            ?: throw IllegalStateException("App is locked")
        val existing = dao.getNoteById(id) ?: return

        val updated = existing.copy(
            encryptedTitle = cryptoManager.encryptString(
                title.ifEmpty { "Untitled" },
                dek
            ),
            encryptedContent = cryptoManager.encryptString(content, dek),
            encryptedTags = if (tags.isNotEmpty()) cryptoManager.encryptString(tags, dek) else "",
            updatedAt = System.currentTimeMillis(),
            colorIndex = colorIndex,
            isPinned = isPinned
        )
        dao.updateNote(updated)
    }

    suspend fun moveToTrash(id: Long) = dao.moveToTrash(id)
    suspend fun restoreFromTrash(id: Long) = dao.restoreFromTrash(id)
    suspend fun permanentlyDelete(id: Long) {
        val note = dao.getNoteById(id) ?: return
        dao.permanentlyDelete(note)
    }

    suspend fun setPinned(id: Long, isPinned: Boolean) = dao.setPinned(id, isPinned)

    suspend fun getActiveNoteCount(): Int = dao.getActiveNoteCount()

    // ───────────── Backup Support ─────────────

    /**
     * Returns all active notes as decrypted NoteBackup objects.
     * Used by BackupRepository.
     */
    suspend fun getAllDecryptedForBackup(): List<NoteBackup> {
        val dek = keyManager.getDataEncryptionKey()
            ?: throw IllegalStateException("App is locked")
        val notes = dao.getAllNotesSnapshot()

        return notes.mapNotNull { note ->
            try {
                NoteBackup(
                    id = note.id,
                    title = cryptoManager.decryptString(note.encryptedTitle, dek),
                    content = cryptoManager.decryptString(note.encryptedContent, dek),
                    tags = if (note.encryptedTags.isNotEmpty()) {
                        cryptoManager.decryptString(note.encryptedTags, dek)
                    } else "",
                    createdAt = note.createdAt,
                    updatedAt = note.updatedAt,
                    isPinned = note.isPinned,
                    colorIndex = note.colorIndex
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Imports notes from backup (re-encrypts with current DEK).
     */
    suspend fun importFromBackup(notes: List<NoteBackup>) {
        val dek = keyManager.getDataEncryptionKey()
            ?: throw IllegalStateException("App is locked")

        for (backup in notes) {
            val note = com.bitnotes.app.data.model.Note(
                encryptedTitle = cryptoManager.encryptString(backup.title, dek),
                encryptedContent = cryptoManager.encryptString(backup.content, dek),
                encryptedTags = if (backup.tags.isNotEmpty()) {
                    cryptoManager.encryptString(backup.tags, dek)
                } else "",
                createdAt = backup.createdAt,
                updatedAt = backup.updatedAt,
                isPinned = backup.isPinned,
                colorIndex = backup.colorIndex
            )
            dao.insertNote(note)
        }
    }
}
