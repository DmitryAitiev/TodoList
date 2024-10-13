package com.example.todolist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface NotesDao {
    @Query("Select* from notes")
    fun getNotes(): MutableList<Note>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(note: Note): Unit
    @Query("delete from notes where id = :id")
    fun remove(id: Int): Unit
}