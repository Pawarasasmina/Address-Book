package com.example.crud_app

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "addressapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "alladdress"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_ADDRESS = "address"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_ADDRESS TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertAddress(address: Address){
        val db = writableDatabase
        val values = ContentValues().apply{
            put(COLUMN_NAME, address.name)
            put(COLUMN_ADDRESS, address.address)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAddress():List<Address>{
        val addressList = mutableListOf<Address>()
        val db = readableDatabase
        val retrievequery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(retrievequery, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val adress = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))

            val address = Address(id, name, adress)
            addressList.add(address)
        }
        cursor.close()
        db.close()
        return addressList
    }

    fun update(address: Address){
        val db = writableDatabase
        val values = ContentValues().apply{
            put(COLUMN_NAME, address.name)
            put(COLUMN_ADDRESS, address.address)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(address.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getAddressById(addressId: Int): Address{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $addressId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
        val adress = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))

        cursor.close()
        db.close()
        return Address(id, name, adress)
    }

    fun deleteAddress(addressId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(addressId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    fun deleteAllAddresses() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
        db.close()
    }


}