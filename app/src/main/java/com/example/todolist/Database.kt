package com.example.todolist

import android.util.Log

class Database() {
    private val notes: MutableList<Note> = mutableListOf()
    fun add(note: Note) = notes.add(note)
    fun remove(id: Int){
        val noteToRemove = notes.find { it.id == id }
        noteToRemove?.let { notes.remove(it) }
    }
    fun getNotes(): MutableList<Note> = notes

    init {
        for (i in 0 until 20) {
            val note = Note(i, "Note " + i, (0..2).random())
            notes.add(note)
            Log.d("SizeOfList", "Size = ${notes.size}")
        }
    }
    companion object {
        private var db: Database? = null
        fun getInstance(): Database {
            db?.let { return it }
            val instance = Database()
            db = instance
            return instance
        }
    }
}