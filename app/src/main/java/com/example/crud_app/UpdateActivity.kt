package com.example.crud_app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crud_app.R

class UpdateActivity : AppCompatActivity() {

    private lateinit var db: NoteDatabaseHelper
    private var noteId: Int = -1
    private lateinit var updateTitleEditText: EditText
    private lateinit var updateContentEditText: EditText
    private lateinit var updateButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        // Initializing database helper
        db = NoteDatabaseHelper(this)

        // Getting note ID from Intent
        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1) {
            Toast.makeText(this, "Error: No note ID provided.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initializing EditTexts and Button using findViewById
        updateTitleEditText = findViewById(R.id.updatetitle)
        updateContentEditText = findViewById(R.id.updatecontent)
        updateButton = findViewById(R.id.UpdateButton)

        // Load the note details into the EditTexts
        val note = db.getnoteById(noteId)
        updateTitleEditText.setText(note.title)
        updateContentEditText.setText(note.content)

        // Setting onClickListener for the update button
        updateButton.setOnClickListener {
            val newTitle = updateTitleEditText.text.toString()
            val newContent = updateContentEditText.text.toString()
            val updatedNote = Note(noteId, newTitle, newContent)
            db.update(updatedNote)
            Toast.makeText(this, "Note Edited!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
