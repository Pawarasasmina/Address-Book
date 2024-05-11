package com.example.crud_app

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.crud_app.R

class MainActivity : AppCompatActivity() {

    private lateinit var db: NoteDatabaseHelper
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var notesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView and adapter
        notesRecyclerView = findViewById(R.id.notesRecycleView)
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        db = NoteDatabaseHelper(this)
        noteAdapter = NoteAdapter(db.getNotes(), this)
        notesRecyclerView.adapter = noteAdapter

        // Initialize the FloatingActionButton using findViewById
        val addButton = findViewById<FloatingActionButton>(R.id.addButton)
        addButton.setOnClickListener {
            // Intent to start AddNoteActivity
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        noteAdapter.refreshData(db.getNotes())
    }
}
