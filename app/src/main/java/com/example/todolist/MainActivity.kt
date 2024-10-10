package com.example.todolist

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.NoteItemBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteDatabase: NoteDatabase
    private lateinit var notesAdapter: NotesAdapter
    private val handler: Handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        noteDatabase = NoteDatabase.getInstance(this)
        binding.buttonAddNote.setOnClickListener {
            startActivity(AddNoteActivity.newIntent(this))
        }
        notesAdapter = NotesAdapter()
        notesAdapter.setOnNoteClickListener(object : NotesAdapter.OnNoteClickListener {
            override fun onNoteClick(note: Note) {
            }
        })
        binding.recycleViewNotes.adapter = notesAdapter
        noteDatabase.notesDao().getNotes().observe(this, object : Observer<List<Note>> {
            override fun onChanged(value: List<Note>) {
                notesAdapter.setNotes(value)
            }
        })
        val itemTouchHelper: ItemTouchHelper = ItemTouchHelper(object :
                                                                        ItemTouchHelper.SimpleCallback(
                                                                        0,
                                                                        ItemTouchHelper.RIGHT or
                                                                        ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note:Note = notesAdapter.getNotes().get(position)
                val thread: Thread = Thread(object : Runnable {
                    override fun run() {
                        noteDatabase.notesDao().remove(note.id)
                        handler.post{showNotes()}
                    }
                })
                thread.start()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recycleViewNotes)
        showNotes()
    }

    override fun onResume() {
        super.onResume()
        showNotes()
    }

    private fun showNotes(){
        val thread = Thread(object : Runnable {
            override fun run() {
                val notes: MutableList<Note> = noteDatabase.notesDao().getNotes()
                handler.post {     notesAdapter.setNotes(notes) }
            }
        })
        thread.start()
    }
}