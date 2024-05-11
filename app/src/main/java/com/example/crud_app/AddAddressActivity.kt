package com.example.crud_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class AddAddressActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var saveButton: ImageView
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        titleEditText = findViewById(R.id.nameEditText)
        contentEditText = findViewById(R.id.adressEditText)
        saveButton = findViewById(R.id.saveButton)

        db = DatabaseHelper(this)

        saveButton.setOnClickListener {
            val name = titleEditText.text.toString()
            val address = contentEditText.text.toString()
            if(name.isNotBlank() && address.isNotBlank()) {
                val address = Address(0, name, address)
                db.insertAddress(address)
                finish()
                Toast.makeText(this, "Details Added", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Name and Address cannot be empty !", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
