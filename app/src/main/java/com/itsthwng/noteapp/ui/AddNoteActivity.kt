package com.itsthwng.noteapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.itsthwng.noteapp.R
import com.itsthwng.noteapp.data.Note
import com.itsthwng.noteapp.data.NoteDatabaseHelper
import com.itsthwng.noteapp.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NoteDatabaseHelper
    private var noteId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = NoteDatabaseHelper(this)
        initializeData()
        registerListeners()
    }

    private fun initializeData() {
        noteId = intent.getIntExtra("note_id", -1)
        if (noteId != -1) {
            val note = db.getNoteById(noteId)
            binding.edtTitle.setText(note?.title)
            binding.edtContent.setText(note?.content)
        }
    }

    private fun registerListeners() {
        binding.saveButton.setOnClickListener {
            val note = getNoteAdd()
            if (note != null) {
                if (noteId == -1) {
                    db.insertNote(note)
                } else {
                    db.updateNote(note)
                }
                finish()
            } else {
                return@setOnClickListener
            }
        }
    }

    private fun getNoteAdd(): Note? {
        val title = binding.edtTitle.text.toString()
        val content = binding.edtContent.text.toString()

        if (title.isBlank() && content.isBlank()) {
            Toast.makeText(this, "Field is not allowed to be empty", Toast.LENGTH_SHORT).show()
            return null
        } else if (noteId == -1) {
            return Note(0, title, content)
        } else return Note(noteId, title, content)
    }
}