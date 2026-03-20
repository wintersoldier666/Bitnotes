package com.bitnotes.app.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.bitnotes.app.data.model.Note
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = true
)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private const val DATABASE_NAME = "bitnotes_encrypted.db"

        fun create(context: Context, passphrase: ByteArray): NotesDatabase {
            val factory = SupportFactory(passphrase)
            return Room.databaseBuilder(
                context,
                NotesDatabase::class.java,
                DATABASE_NAME
            )
                .openHelperFactory(factory)
                .fallbackToDestructiveMigration()
                .build()
        }

        /**
         * Creates database without encryption for initial setup only.
         * Immediately migrated to encrypted version.
         */
        fun createUnencrypted(context: Context): NotesDatabase {
            return Room.inMemoryDatabaseBuilder(
                context,
                NotesDatabase::class.java
            ).build()
        }
    }
}
