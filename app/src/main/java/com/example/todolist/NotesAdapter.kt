package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.NoteItemBinding
import org.w3c.dom.Text

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private var notes: MutableList<Note> = mutableListOf()
    private var onNoteClickListener: OnNoteClickListener?= null

    fun setOnNoteClickListener(onNoteClickListener: OnNoteClickListener?) {
        this.onNoteClickListener = onNoteClickListener
    }
    fun getNotes(): MutableList<Note> = notes.toMutableList()

    fun setNotes(notes: MutableList<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder{
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.note_item,
            parent,
            false
        )
        return NotesViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: NotesViewHolder, position: Int) {
        val note: Note = notes.get(position)
        viewHolder.textViewNote.text = note.text
        val colorResId = when (note.priority) {
            0 -> android.R.color.holo_green_light
            1 -> android.R.color.holo_orange_light
            2 -> android.R.color.holo_red_light
            else -> android.R.color.transparent
        }
        val color = ContextCompat.getColor(viewHolder.itemView.context, colorResId)
        viewHolder.textViewNote.setBackgroundColor(color)
        viewHolder.itemView.setOnClickListener {
            onNoteClickListener?.onNoteClick(note)
        }
    }
    override fun getItemCount(): Int {
        return notes.size
    }

    class NotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textViewNote = itemView.findViewById<TextView>(R.id.textViewNote)
    }
    interface OnNoteClickListener {
        fun onNoteClick(note: Note)
    }
}