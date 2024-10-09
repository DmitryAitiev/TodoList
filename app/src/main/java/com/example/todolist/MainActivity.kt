package com.example.todolist

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.NoteItemBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val db = Database.getInstance()
    private lateinit var notesAdapter: NotesAdapter
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
        binding.buttonAddNote.setOnClickListener {
            startActivity(AddNoteActivity.newIntent(this))
        }
        notesAdapter = NotesAdapter()
        notesAdapter.setOnNoteClickListener(object : NotesAdapter.OnNoteClickListener {
            override fun onNoteClick(note: Note) {
            }
        })
        binding.recycleViewNotes.adapter = notesAdapter
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
                db.remove(note.id)
                showNotes()
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
        notesAdapter.setNotes(db.getNotes())
    }
}