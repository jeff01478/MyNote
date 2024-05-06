package com.example.mynote

import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

class NoteAdapter(activity: Activity, val resourceId: Int, data: List<Note>, listView: ListView) :
    ArrayAdapter<Note>(activity, resourceId, data) {

    private val listView = listView
    private var noteId = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resourceId, parent, false)
        val noteTitle: TextView = view.findViewById(R.id.noteTitle)
        val noteContent: TextView = view.findViewById(R.id.noteContent)
        val testId: TextView = view.findViewById(R.id.testId)
        val note = getItem(position)
        if (note != null) {
            noteTitle.text = note.title
            noteContent.text = note.content
            testId.text = note.id.toString()
            noteId = note.id
        }
        if (listView.isItemChecked(position)) {
            view.setBackgroundColor(Color.RED)
            MainActivity.multipeId.add(noteId)
            Log.d("multipeId", "multipeId: " + MainActivity.multipeId)
        } else {
            view.setBackgroundColor(Color.TRANSPARENT);
            MainActivity.multipeId.remove(noteId)
            Log.d("multipeId", "multipeId: " + MainActivity.multipeId)
        }
        return view
    }
}