package com.example.todolist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todolist.databinding.ActivityAddNoteBinding
import com.example.todolist.databinding.ActivityMainBinding

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private val db = Database.getInstance()
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
        binding.buttonSave.setOnClickListener {
            saveNote()
        }
    }
    private fun saveNote() {
        val text = binding.editTextNote.text.toString().trim()
        val priority = getPriority()
        val id = db.getNotes().size
        val note = Note(id, text, priority)
        db.add(note)
        finish()
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