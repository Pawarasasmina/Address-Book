package com.example.crud_app

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UpdateActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private var addressId: Int = -1
    private lateinit var updateNameEditText: EditText
    private lateinit var updateAddressEditText: EditText
    private lateinit var updateButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        // Initializing database helper
        db = DatabaseHelper(this)

        // Getting note ID from Intent
        addressId = intent.getIntExtra("address_id", -1)
        if (addressId == -1) {
            Toast.makeText(this, "Error: No Address ID provided.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initializing EditTexts and Button using findViewById
        updateNameEditText = findViewById(R.id.updatename)
        updateAddressEditText = findViewById(R.id.updateadress)
        updateButton = findViewById(R.id.UpdateButton)

        // Load the note details into the EditTexts
        val adress = db.getAddressById(addressId)
        updateNameEditText.setText(adress.name)
        updateAddressEditText.setText(adress.address)

        // Setting onClickListener for the update button
        updateButton.setOnClickListener {
            val newName = updateNameEditText.text.toString()
            val newAddress = updateAddressEditText.text.toString()
            if(newName.isNotBlank() && newAddress.isNotBlank()) {
                val updatedAddress = Address(addressId, newName, newAddress)
                db.update(updatedAddress)
                Toast.makeText(this, "Address Edited!", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Name and Address cannot be empty !", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
