package com.example.myapplication

import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var enter: Button
    private lateinit var login: Button
//    private lateinit var myDB : SQLiteDatabase

//    fun OpenDB() {
//        val sql: SQLiteOpenHelper = object : SQLiteOpenHelper(this, "my_db", null, 1) {
//            override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
//            override fun onCreate(db: SQLiteDatabase) {}
//        }
//        myDB = sql.writableDatabase
//        myDB.execSQL("CREATE TABLE notes (text TEXT, date Text, title Text)")
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
//        OpenDB()
    }

    fun initViews(){
        login = findViewById(R.id.login)
        enter = findViewById(R.id.enter)

        login.setOnClickListener(this)
        enter.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.enter -> startActivity(Intent(this, Notes::class.java))
                R.id.login -> login()
            }
        }
    }

    private fun login() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Login")
        val view = layoutInflater.inflate(R.layout.login_dialog, null)
        val pref = this.getSharedPreferences("prefs", 0)

        view.findViewById<EditText>(R.id.username).setText(pref.getString("username", ""))
        view.findViewById<EditText>(R.id.password).setText(pref.getString("password", ""))

        alertBuilder.setView(view)
        alertBuilder.setPositiveButton("Login") { dialogInterface: DialogInterface, i: Int ->
            val username = view.findViewById<EditText>(R.id.username).text
            val pass = view.findViewById<EditText>(R.id.password).text
            val prefs = getSharedPreferences("prefs", 0)
            val edit = prefs.edit()
            edit.putString("username", username.toString())
            edit.putString("password", pass.toString())
            edit.commit()
            startActivity(Intent(this, Notes::class.java))
        }

        alertBuilder.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.cancel()
        }
        alertBuilder.create()
        alertBuilder.show()
    }


}