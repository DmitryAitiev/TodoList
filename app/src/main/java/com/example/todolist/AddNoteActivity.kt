package com.example.todolist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todolist.databinding.ActivityAddNoteBinding
import com.example.todolist.databinding.ActivityMainBinding

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NoteDatabase
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = NoteDatabase.getInstance(this)
        binding.buttonSave.setOnClickListener {
            saveNote()
        }
    }
    private fun saveNote() {
        val text = binding.editTextNote.text.toString().trim()
        val priority = getPriority()
        val note = Note(text = text, priority = priority)
        val thread = Thread(object : Runnable {
            override fun run() {
                db.notesDao().add(note)
                finish()
            }
        })
        thread.start()
    }
    private fun getPriority(): Int {
        val priority = if (binding.rbLow.isChecked) 0
        else if(binding.rbMedium.isChecked) 1
        else 2
        return priority
    }
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AddNoteActivity::class.java)
        }
    }
}