package com.example.crud_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var adapter: Adapter
    private lateinit var addressRecyclerView: RecyclerView
    private lateinit var sortingOptions: Spinner // Add Spinner for sorting options


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView and adapter
        addressRecyclerView = findViewById(R.id.notesRecycleView)
        addressRecyclerView.layoutManager = LinearLayoutManager(this)
        db = DatabaseHelper(this)
        adapter = Adapter(db.getAddress(), this)
        addressRecyclerView.adapter = adapter

        // Initialize the FloatingActionButton using findViewById
        val addButton = findViewById<FloatingActionButton>(R.id.addButton)
        addButton.setOnClickListener {
            // Intent to start AddNoteActivity
            val intent = Intent(this, AddAddressActivity::class.java)
            startActivity(intent)
        }
        // Find the "Delete All" button by ID
        val deleteAllButton = findViewById<Button>(R.id.deleteAllButton)

        // Set click listener for the "Delete All" button
        deleteAllButton.setOnClickListener {
            // Show confirmation dialog
            showDeleteAllConfirmationDialog()
        }
// Initialize sorting options spinner
        sortingOptions = findViewById(R.id.sortingOptions)
        ArrayAdapter.createFromResource(
            this,
            R.array.sorting_options_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sortingOptions.adapter = adapter
        }

        sortingOptions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Sort notes based on the selected sorting option
                when (position) {
                    0 -> adapter.sortByCreationTime()
                    1 -> adapter.sortAlphabetically()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing if nothing is selected
            }
        }


    }

    override fun onResume() {
        super.onResume()
        val sortedNotes = db.getAddress().sortedByDescending { it.id } // or sortedByDescending { it.id } for creation time order
        adapter.refreshData(sortedNotes)
    }

    private fun showDeleteAllConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Delete All Addresses")
        alertDialogBuilder.setMessage("Are you sure you want to delete all addresses?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // Delete all addresses
            db.deleteAllAddresses()
            // Refresh RecyclerView
            adapter.refreshData(db.getAddress())
            // Show toast message
            Toast.makeText(this, "All addresses deleted", Toast.LENGTH_SHORT).show()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
