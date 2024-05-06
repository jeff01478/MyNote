package com.example.mynote

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    companion object {
        private val noteList = ArrayList<Note>()
        private var launch = false
        val multipeId = mutableSetOf<Int>()
        var Position = 0
        var instance: MainActivity? = null
    }

    lateinit var addNote: Button
    lateinit var noteListView: ListView
    lateinit var mainBar: LinearLayout

    private val dbHelper = NoteDatabaseHelper(this, "Note.db", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        instance = this
        if (!launch) {
            queryNote()
            launch = true
        }
        initObject()
        val adapter = NoteAdapter(this, R.layout.note_item, noteList, noteListView)
        noteListView.adapter = adapter
        noteListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
        val callback = ModeCallBack(this, noteListView, noteList, adapter, dbHelper)
        addNote.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            intent.putExtra("mod", 0)
            startActivity(intent)
        }
        noteListView.setOnItemClickListener { _, _, position, _ ->
            val note = noteList[position]
            Position = position
            val intent = Intent(this, NoteActivity::class.java)
            intent.putExtra("title", note.title)
            intent.putExtra("content", note.content)
            intent.putExtra("mod", 1)
            startActivity(intent)
        }
        noteListView.setMultiChoiceModeListener(callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        var extraTitle = ""
        var extraContent = ""
        val extraMod = intent?.getIntExtra("mod", 0)
        if (intent != null) {
            if (intent.getStringExtra("title") != null)
                extraTitle = intent.getStringExtra("title")!!
        }
        if (intent != null) {
            if (intent.getStringExtra("content") != null)
                extraContent = intent.getStringExtra("content")!!
        }
        if (extraTitle != "" || extraContent != "") {
            when (extraMod) {
                0 -> addNote(extraTitle, extraContent)
                1 -> updateNote(extraTitle, extraContent)
                2 -> deleteNote()
            }
        }

        val adapter = NoteAdapter(this, R.layout.note_item, noteList, noteListView)
        noteListView.adapter = adapter
        val callback = ModeCallBack(this, noteListView, noteList, adapter, dbHelper)
        noteListView.setMultiChoiceModeListener(callback)
        adapter.notifyDataSetChanged()
    }

    private fun initObject() {
        addNote = findViewById(R.id.addNote)
        noteListView = findViewById(R.id.noteListView)
        mainBar = findViewById(R.id.mainBar)
    }

    @SuppressLint("Range")
    private fun queryNote() {
        val db = dbHelper.writableDatabase
        val cursor = db.query("Note", null, null, null,
            null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                noteList.add(Note(id, title, content))
            } while (cursor.moveToNext())
        }
    }

    private fun addNote(title: String, content: String) {
        var id = 0
        if (noteList.size != 0)
            id = noteList[noteList.size - 1].id + 1
        noteList.add(Note(id, title, content))
        val db = dbHelper.writableDatabase
        val newNote = ContentValues().apply {
            put("id", id)
            put("title", title)
            put("content", content)
        }
        db.insert("Note", null, newNote)
    }

    private fun updateNote(title: String, content: String) {
        noteList[Position] = Note(noteList[Position].id, title, content)
        val db = dbHelper.writableDatabase
        val updateNote = ContentValues().apply {
            put("title", title)
            put("content", content)
        }
        db.update("Note", updateNote, "id = ?", arrayOf(noteList[Position].id.toString()))
    }

    private fun deleteNote() {
        Log.d("GOOD", noteList[Position].id.toString())
        val db = dbHelper.writableDatabase
        db.delete("Note", "id = ?", arrayOf(noteList[Position].id.toString()))
        noteList.removeAt(Position)
    }

    fun deleteMultipeNote(dbHelper: NoteDatabaseHelper) {
        val db = dbHelper.writableDatabase
        multipeId.forEach {
            db.delete("Note", "id = ?", arrayOf(it.toString()))
            noteList.removeIf { note ->
                note.id == it
            }
        }
        multipeId.clear()
    }
}
