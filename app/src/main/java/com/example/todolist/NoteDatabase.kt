package com.example.todolist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [Note::class], version = 2, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    companion object {
        private var instance: NoteDatabase? = null
        private const val DB_NAME = "notes.db"
        fun getInstance(context: Context): NoteDatabase {
            instance?.let { return it }
            val db =
                Room.databaseBuilder(context, NoteDatabase::class.java, DB_NAME).fallbackToDestructiveMigration().build()
            instance = db
            return db
        }
    }
    abstract fun notesDao(): NotesDao
}