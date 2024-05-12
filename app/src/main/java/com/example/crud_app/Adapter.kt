package com.example.crud_app

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class Adapter (private var addresses :List<Address>, private val context: Context) : RecyclerView.Adapter<Adapter.NoteViewHolder>() {

    private val db: DatabaseHelper = DatabaseHelper(context)
    private var addressesFull: List<Address> = ArrayList(addresses)
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.addressTextView)
        val update: ImageView = itemView.findViewById(R.id.UpdateButton)
        val delete: ImageView = itemView.findViewById(R.id.deleteButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.address_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = addresses.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val address = addresses[position]
        holder.titleTextView.text = address.name
        holder.contentTextView.text = address.address

        holder.update.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateActivity::class.java).apply {
                putExtra("address_id", address.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.delete.setOnClickListener {
            db.deleteAddress(address.id)
            refreshData(db.getAddress())
            Toast.makeText(holder.itemView.context, "Address Deleted", Toast.LENGTH_SHORT).show()

        }
        holder.delete.setOnClickListener {
            showDeleteConfirmationDialog(address.id)
        }


    }
    private fun showDeleteConfirmationDialog(addressId: Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Delete Address")
        alertDialogBuilder.setMessage("Are you sure you want to delete this address?")
        alertDialogBuilder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            db.deleteAddress(addressId)
            refreshData(db.getAddress())
            Toast.makeText(context, "Address Deleted", Toast.LENGTH_SHORT).show()
        }
        alertDialogBuilder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            // Do nothing
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    // Method to sort notes alphabetically
    fun sortAlphabetically() {
        addresses = addresses.sortedBy { it.name } // Sorting notes by title alphabetically
        notifyDataSetChanged() // Notify RecyclerView about the dataset change
    }

    // Method to sort notes by creation time
    fun sortByCreationTime() {
        addresses = addresses.sortedByDescending { it.id } // Sorting notes by id (assuming id represents creation time)
        notifyDataSetChanged() // Notify RecyclerView about the dataset change
    }

    fun refreshData(newAddress: List<Address>) {
        addresses = newAddress
        notifyDataSetChanged()
    }


    fun filter(query: String) {
        addresses = if (query.isEmpty()) {
            addressesFull
        } else {
            val resultList = ArrayList<Address>()
            for (address in addressesFull) {
                if (address.name.lowercase().contains(query.lowercase())) {
                    resultList.add(address)
                }
            }
            resultList
        }
        notifyDataSetChanged()
    }
}