package com.example.todolist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotesDao {
    @Query("Select* from notes")
    fun getNotes(): LiveData<MutableList<Note>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(note: Note): Unit
    @Query("delete from notes where id = :id")
    fun remove(id: Int): Unit
}