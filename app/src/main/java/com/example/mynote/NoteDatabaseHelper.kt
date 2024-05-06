package com.example.mynote

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class NoteDatabaseHelper(val context: MainActivity, name: String, version: Int) :
        SQLiteOpenHelper(context, name, null, version) {

    private val createNote = "create table Note (" +
                             "id integer primary key, " +
                             "title text, " +
                             "content text)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createNote)
        Toast.makeText(context, "Create succeeded", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}