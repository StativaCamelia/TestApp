package com.example.myapplication

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime

class Notes:AppCompatActivity(), View.OnClickListener {

    private lateinit var notes: ListView
    private lateinit var myDB : SQLiteDatabase
    val titles = mutableListOf<String>()
    val dates = mutableListOf<String>()
    val texts = mutableListOf<String>()
    private lateinit var addButton: Button

    fun readData(){
        val sql: SQLiteOpenHelper = object : SQLiteOpenHelper(this, "my_db", null, 1) {
            override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
            override fun onCreate(db: SQLiteDatabase) {}
        }
        myDB = sql.writableDatabase
        val cursor = myDB.query(
            "notes",
            arrayOf("text", "date", "title"),
            null,
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        with(cursor) {
            while (moveToNext()) {
                val date = getString(1)
                val text = getString(0)
                val title = getString(2)
                titles.add(title)
                texts.add(text)
                dates.add(date)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notes)
        notes = findViewById(R.id.list_view)
        readData()
        notes.adapter = AdapterNotes(this, titles, dates, texts)
        addButton = findViewById(R.id.add)
        addButton.setOnClickListener(this)
        notes.setOnItemClickListener(){ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            openAlertItem(i)
        }

    }

    private fun openAlertItem(item: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("What do you want?")
        builder.setPositiveButton("Open", { dialogInterface: DialogInterface, i: Int -> })
        builder.setNegativeButton("Delete") { dialogInterface: DialogInterface, i: Int ->
            myDB.delete("notes", "title", arrayOf(titles[item]))
        }
        builder.create()
        builder.show()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.add ->{
                insertNote()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertNote() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Note")
        val view = layoutInflater.inflate(R.layout.note_input, null)
        builder.setView(view)
        builder.setPositiveButton("Save") { dialogInterface: DialogInterface, i: Int ->
            val title = view.findViewById<EditText>(R.id.noteTitle).text.toString()
            val content = view.findViewById<EditText>(R.id.noteContent).text.toString()
            val current = LocalDateTime.now().toString()

            val values = ContentValues().apply {
                put("title", title)
                put("date", current)
                put("text", content)
            }

            val newRowId = myDB?.insert("notes", null, values)
            Toast.makeText(this, "Note created", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Notes::class.java))
        }
        builder.setNegativeButton("Discard", { dialogInterface: DialogInterface, i: Int -> })
        builder.create()
        builder.show()
    }


}