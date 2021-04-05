package com.example.myapplication

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdapterNotes(val context: Activity, val description: MutableList<String>, val date: MutableList<String>, val title: MutableList<String>): ArrayAdapter<String>(context, R.layout.note, title) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = context.layoutInflater.inflate(R.layout.note, null, true)
        view.findViewById<TextView>(R.id.title).text = title[position]
        view.findViewById<TextView>(R.id.description).text = description[position]
        view.findViewById<TextView>(R.id.date).text = date[position]
        return view
    }
}