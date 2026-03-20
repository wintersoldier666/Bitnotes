package com.bitnotes.app.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bitnotes.app.data.model.Note
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the Room database instance.
 * Uses WAL mode for performance and standard AES-256 column-level encryption
 * (since notes content is already encrypted before storage).
 */
@Singleton
class DatabaseManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    @Volatile
    private var database: AppDatabase? = null

    fun getDatabase(): AppDatabase {
        return database ?: synchronized(this) {
            database ?: buildDatabase().also { database = it }
        }
    }

    private fun buildDatabase(): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bitnotes.db"
        )
            .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .build()
    }
}

@androidx.room.Database(
    entities = [Note::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
