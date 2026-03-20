package com.bitnotes.app.data.repository;

import com.bitnotes.app.crypto.CryptoManager;
import com.bitnotes.app.crypto.KeyManager;
import com.bitnotes.app.data.db.DatabaseManager;
import com.bitnotes.app.data.model.*;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles encrypt/decrypt operations transparently.
 * Notes are always encrypted before storage and decrypted on retrieval.
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ2\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u0012\u001a\u00020\u00102\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0015J\u000e\u0010\u0016\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0017J\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019H\u0086@\u00a2\u0006\u0002\u0010\u0017J\u0012\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001d0\u00190\u001cJ\u0018\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\u0006\u0010 \u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010!J\u0012\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001d0\u00190\u001cJ\u001c\u0010#\u001a\u00020$2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019H\u0086@\u00a2\u0006\u0002\u0010&J\u0016\u0010\'\u001a\u00020$2\u0006\u0010 \u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010!J\u0016\u0010(\u001a\u00020$2\u0006\u0010 \u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010!J\u0016\u0010)\u001a\u00020$2\u0006\u0010 \u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010!J\u001e\u0010*\u001a\u00020$2\u0006\u0010 \u001a\u00020\u000e2\u0006\u0010+\u001a\u00020,H\u0086@\u00a2\u0006\u0002\u0010-JD\u0010.\u001a\u00020$2\u0006\u0010 \u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u0012\u001a\u00020\u00102\b\b\u0002\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010+\u001a\u00020,H\u0086@\u00a2\u0006\u0002\u0010/R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\n8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2 = {"Lcom/bitnotes/app/data/repository/NoteRepository;", "", "databaseManager", "Lcom/bitnotes/app/data/db/DatabaseManager;", "cryptoManager", "Lcom/bitnotes/app/crypto/CryptoManager;", "keyManager", "Lcom/bitnotes/app/crypto/KeyManager;", "(Lcom/bitnotes/app/data/db/DatabaseManager;Lcom/bitnotes/app/crypto/CryptoManager;Lcom/bitnotes/app/crypto/KeyManager;)V", "dao", "Lcom/bitnotes/app/data/db/NoteDao;", "getDao", "()Lcom/bitnotes/app/data/db/NoteDao;", "createNote", "", "title", "", "content", "tags", "colorIndex", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveNoteCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllDecryptedForBackup", "", "Lcom/bitnotes/app/data/model/NoteBackup;", "getAllNotePreviews", "Lkotlinx/coroutines/flow/Flow;", "Lcom/bitnotes/app/data/model/NotePreview;", "getNoteById", "Lcom/bitnotes/app/data/model/DecryptedNote;", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTrashPreviews", "importFromBackup", "", "notes", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "moveToTrash", "permanentlyDelete", "restoreFromTrash", "setPinned", "isPinned", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateNote", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;IZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class NoteRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.bitnotes.app.data.db.DatabaseManager databaseManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bitnotes.app.crypto.CryptoManager cryptoManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bitnotes.app.crypto.KeyManager keyManager = null;
    
    @javax.inject.Inject()
    public NoteRepository(@org.jetbrains.annotations.NotNull()
    com.bitnotes.app.data.db.DatabaseManager databaseManager, @org.jetbrains.annotations.NotNull()
    com.bitnotes.app.crypto.CryptoManager cryptoManager, @org.jetbrains.annotations.NotNull()
    com.bitnotes.app.crypto.KeyManager keyManager) {
        super();
    }
    
    private final com.bitnotes.app.data.db.NoteDao getDao() {
        return null;
    }
    
    /**
     * Returns a flow of decrypted note previews for the list screen.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.bitnotes.app.data.model.NotePreview>> getAllNotePreviews() {
        return null;
    }
    
    /**
     * Returns a flow of decrypted notes in trash.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.bitnotes.app.data.model.NotePreview>> getTrashPreviews() {
        return null;
    }
    
    /**
     * Gets a single decrypted note by ID.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getNoteById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bitnotes.app.data.model.DecryptedNote> $completion) {
        return null;
    }
    
    /**
     * Creates a new encrypted note.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createNote(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String content, @org.jetbrains.annotations.NotNull()
    java.lang.String tags, int colorIndex, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    /**
     * Updates an existing note with new encrypted content.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateNote(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String content, @org.jetbrains.annotations.NotNull()
    java.lang.String tags, int colorIndex, boolean isPinned, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object moveToTrash(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object restoreFromTrash(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object permanentlyDelete(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setPinned(long id, boolean isPinned, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getActiveNoteCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    /**
     * Returns all active notes as decrypted NoteBackup objects.
     * Used by BackupRepository.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAllDecryptedForBackup(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.bitnotes.app.data.model.NoteBackup>> $completion) {
        return null;
    }
    
    /**
     * Imports notes from backup (re-encrypts with current DEK).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object importFromBackup(@org.jetbrains.annotations.NotNull()
    java.util.List<com.bitnotes.app.data.model.NoteBackup> notes, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}