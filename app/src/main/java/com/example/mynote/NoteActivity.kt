package com.example.mynote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class NoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        val back: Button = findViewById(R.id.back)
        val save: Button = findViewById(R.id.save)
        val delete: Button = findViewById(R.id.delete)
        val title: EditText = findViewById(R.id.title)
        val content: EditText = findViewById(R.id.content)
        val extraMod = intent.getIntExtra("mod", 0)

        if (extraMod == 1) {
            val extraTitle = intent.getStringExtra("title")
            val extraContent = intent.getStringExtra("content")
            title.setText(extraTitle)
            content.setText(extraContent)
            delete.visibility = View.VISIBLE
        }

        back.setOnClickListener {
            finish()
        }

        save.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("title", title.text.toString())
            intent.putExtra("content", content.text.toString())
            intent.putExtra("mod", extraMod)
            startActivity(intent)
        }

        delete.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("警告")
                setMessage("確定要將此筆記刪除?")
                setPositiveButton("確認") { _, _ ->
                    val intent = Intent(this@NoteActivity, MainActivity::class.java)
                    intent.putExtra("title", title.text.toString())
                    intent.putExtra("content", content.text.toString())
                    intent.putExtra("mod", 2)
                    startActivity(intent)
                }
                setNegativeButton("取消") { _, _ ->

                }
                show()
            }
        }
    }
}