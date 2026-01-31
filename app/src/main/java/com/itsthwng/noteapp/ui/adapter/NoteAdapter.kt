package com.itsthwng.noteapp.ui.adapter

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.itsthwng.noteapp.R
import com.itsthwng.noteapp.data.Note
import com.itsthwng.noteapp.data.NoteDatabaseHelper
import com.itsthwng.noteapp.databinding.NoteItemBinding
import com.itsthwng.noteapp.ui.AddNoteActivity

class NoteAdapter(val context: Context, val db: NoteDatabaseHelper) : ListAdapter<Note, NoteAdapter.ViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(
                oldItem: Note,
                newItem: Note
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Note,
                newItem: Note
            ): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.title == newItem.title &&
                        oldItem.content == newItem.content
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = NoteItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Note) {
            binding.tvTitle.text = item.title
            binding.tvContent.text = item.content

            binding.btnDelete.setOnClickListener {
                db.deleteNote(item.id)
                submitList(db.getAllNotes())
            }

            binding.root.setOnClickListener {
                val intent = Intent(it.context, AddNoteActivity::class.java).apply {
                    putExtra("note_id", item.id)
                }
                val options = ActivityOptions.makeCustomAnimation(
                    it.context,
                    R.anim.slide_in_bottom,
                    R.anim.slide_out_top
                )
                context.startActivity(intent, options.toBundle())
            }
        }
    }
}