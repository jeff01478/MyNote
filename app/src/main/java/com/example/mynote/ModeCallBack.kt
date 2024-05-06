package com.example.mynote

import android.content.Context
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import android.widget.EditText

class ModeCallBack(context: Context,
                   listView: ListView,
                   noteArray: ArrayList<Note>,
                   adapter: NoteAdapter,
                   dbHelper: NoteDatabaseHelper) : AbsListView.MultiChoiceModeListener {

    private lateinit var actionBarView: View
    private lateinit var selectedNum: TextView
    private lateinit var actionBar_delete: Button

    private val context = context
    private val listView = listView
    private val noteArray = noteArray
    private val adapter = adapter
    private val dbHelper = dbHelper
    private val mainActivity = MainActivity.instance

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        actionBarView = LayoutInflater.from(context).inflate(R.layout.actionbar_view, null)
        selectedNum = actionBarView.findViewById(R.id.selected_num)
        actionBar_delete = actionBarView.findViewById(R.id.actionBar_delete)
        mode?.customView = actionBarView
        mainActivity?.let {
            it.mainBar.visibility = View.GONE
        }
        actionBar_delete.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle("警告")
                setMessage("確定要將選擇的筆記刪除?")
                setPositiveButton("確認") { _, _ ->
                    val mainActivity = MainActivity()
                    mainActivity.deleteMultipeNote(dbHelper)
                    mode?.finish()
                }
                setNegativeButton("取消") { _, _ ->

                }
                show()
            }
            adapter.notifyDataSetChanged()
        }
        return true
    }

    override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
        Log.d("GOOD", "onPrepareActionMode")
        return true
    }

    override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
        return true
    }

    override fun onDestroyActionMode(p0: ActionMode?) {
        mainActivity?.let {
            it.mainBar.visibility = View.VISIBLE
        }
    }

    override fun onItemCheckedStateChanged(mode: ActionMode?, position: Int, long: Long, boolean: Boolean) {
        val selectCount = listView.checkedItemCount
        selectedNum.text = " " + selectCount
        mode?.invalidate()
        adapter.notifyDataSetChanged()
    }
}