package com.bitnotes.app.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Encrypted note entity stored in Room database.
 *
 * ALL text fields are stored encrypted using AES-256-GCM.
 * The plaintext NEVER touches persistent storage.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\"\b\u0087\b\u0018\u00002\u00020\u0001Bg\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000b\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0010J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010 \u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0016J\t\u0010!\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\"\u001a\u00020\u0005H\u00c6\u0003J\t\u0010#\u001a\u00020\u0005H\u00c6\u0003J\t\u0010$\u001a\u00020\u0003H\u00c6\u0003J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\u000bH\u00c6\u0003J\t\u0010\'\u001a\u00020\rH\u00c6\u0003J\t\u0010(\u001a\u00020\u000bH\u00c6\u0003Jt\u0010)\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000b2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001\u00a2\u0006\u0002\u0010*J\u0013\u0010+\u001a\u00020\u000b2\b\u0010,\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010-\u001a\u00020\rH\u00d6\u0001J\t\u0010.\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0015\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0017\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0019R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0019R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0014R\u0011\u0010\u000e\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u001dR\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u001dR\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0014\u00a8\u0006/"}, d2 = {"Lcom/bitnotes/app/data/model/Note;", "", "id", "", "encryptedTitle", "", "encryptedContent", "encryptedTags", "createdAt", "updatedAt", "isPinned", "", "colorIndex", "", "isDeleted", "deletedAt", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;JJZIZLjava/lang/Long;)V", "getColorIndex", "()I", "getCreatedAt", "()J", "getDeletedAt", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getEncryptedContent", "()Ljava/lang/String;", "getEncryptedTags", "getEncryptedTitle", "getId", "()Z", "getUpdatedAt", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;JJZIZLjava/lang/Long;)Lcom/bitnotes/app/data/model/Note;", "equals", "other", "hashCode", "toString", "app_debug"})
@androidx.room.Entity(tableName = "notes")
public final class Note {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;
    
    /**
     * Encrypted note title (Base64-encoded AES-GCM ciphertext)
     */
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String encryptedTitle = null;
    
    /**
     * Encrypted note content (Base64-encoded AES-GCM ciphertext)
     */
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String encryptedContent = null;
    
    /**
     * Encrypted tags/category (Base64-encoded AES-GCM ciphertext)
     */
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String encryptedTags = null;
    
    /**
     * Creation timestamp (not sensitive, stored as-is)
     */
    private final long createdAt = 0L;
    
    /**
     * Last modified timestamp
     */
    private final long updatedAt = 0L;
    
    /**
     * Whether this note is pinned to top
     */
    private final boolean isPinned = false;
    
    /**
     * Color index for note card (0 = default)
     */
    private final int colorIndex = 0;
    
    /**
     * Whether note is in trash
     */
    private final boolean isDeleted = false;
    
    /**
     * When note was moved to trash
     */
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long deletedAt = null;
    
    public Note(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String encryptedTitle, @org.jetbrains.annotations.NotNull()
    java.lang.String encryptedContent, @org.jetbrains.annotations.NotNull()
    java.lang.String encryptedTags, long createdAt, long updatedAt, boolean isPinned, int colorIndex, boolean isDeleted, @org.jetbrains.annotations.Nullable()
    java.lang.Long deletedAt) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    /**
     * Encrypted note title (Base64-encoded AES-GCM ciphertext)
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEncryptedTitle() {
        return null;
    }
    
    /**
     * Encrypted note content (Base64-encoded AES-GCM ciphertext)
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEncryptedContent() {
        return null;
    }
    
    /**
     * Encrypted tags/category (Base64-encoded AES-GCM ciphertext)
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEncryptedTags() {
        return null;
    }
    
    /**
     * Creation timestamp (not sensitive, stored as-is)
     */
    public final long getCreatedAt() {
        return 0L;
    }
    
    /**
     * Last modified timestamp
     */
    public final long getUpdatedAt() {
        return 0L;
    }
    
    /**
     * Whether this note is pinned to top
     */
    public final boolean isPinned() {
        return false;
    }
    
    /**
     * Color index for note card (0 = default)
     */
    public final int getColorIndex() {
        return 0;
    }
    
    /**
     * Whether note is in trash
     */
    public final boolean isDeleted() {
        return false;
    }
    
    /**
     * When note was moved to trash
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getDeletedAt() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    public final long component5() {
        return 0L;
    }
    
    public final long component6() {
        return 0L;
    }
    
    public final boolean component7() {
        return false;
    }
    
    public final int component8() {
        return 0;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bitnotes.app.data.model.Note copy(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String encryptedTitle, @org.jetbrains.annotations.NotNull()
    java.lang.String encryptedContent, @org.jetbrains.annotations.NotNull()
    java.lang.String encryptedTags, long createdAt, long updatedAt, boolean isPinned, int colorIndex, boolean isDeleted, @org.jetbrains.annotations.Nullable()
    java.lang.Long deletedAt) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}