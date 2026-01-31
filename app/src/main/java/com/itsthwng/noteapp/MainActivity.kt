package com.itsthwng.noteapp

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.itsthwng.noteapp.data.NoteDatabaseHelper
import com.itsthwng.noteapp.databinding.ActivityMainBinding
import com.itsthwng.noteapp.ui.AddNoteActivity
import com.itsthwng.noteapp.ui.adapter.NoteAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: NoteDatabaseHelper
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBar = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBar.left, systemBar.top, systemBar.right, systemBar.bottom)
            insets
        }

        db = NoteDatabaseHelper(this)
        noteAdapter = NoteAdapter(this, db)

        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.adapter = noteAdapter

        registerListeners()
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    private fun loadNotes() {
        val notes = db.getAllNotes()
        noteAdapter.submitList(notes)
        Log.d("ThangLT", "notes size = ${notes.size}")
    }

    private fun registerListeners() {
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this, R.anim.slide_in_bottom, R.anim.slide_out_top
            )
            startActivity(intent, options.toBundle())
        }
    }
}
