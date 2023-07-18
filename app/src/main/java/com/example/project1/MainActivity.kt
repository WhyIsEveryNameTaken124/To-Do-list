package com.example.project1

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    var itemsList:ArrayList<String> = arrayListOf()
    lateinit var arrayAdapter:ArrayAdapter<String>

    lateinit var editText: EditText
    lateinit var button: Button
    lateinit var listView: ListView

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.editTextText)
        button = findViewById(R.id.button)
        /*editText2 = findViewById(R.id.editTextText2)  //crashes
        button2 = findViewById(R.id.button2)*/
        /*button2 = findViewById(R.id.button2)
        button2 = findViewById(R.id.button2)*/
        listView = findViewById(R.id.listView)

        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        button.setOnClickListener {
            if(editText.text.isNotEmpty()) {
                itemsList.add(itemsList.size, editText.text.toString())
                arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemsList)
                listView.adapter = arrayAdapter
                editText.text = null
                editor.putStringSet("my list", itemsList.toSet())
            }
        }
/*
        button2.setOnClickListener {
            if(editText2.text.isNotEmpty()) {
                itemsList.add(itemsList.size, editText2.text.toString())
                arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemsList)
                listView.adapter = arrayAdapter
                editText2.text = null
                editor.putStringSet("my list", itemsList.toSet())
            }
        }
*/
        listView.setOnItemLongClickListener { adapterView, view, index, l ->
            val alertDialog = AlertDialog.Builder(this)
                .setTitle(R.string.popup_main_text)
                .setPositiveButton(R.string.popup_yes) { dialog, _ ->
                    itemsList.removeAt(index)
                    arrayAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, itemsList)
                    listView.adapter = arrayAdapter
                    editor.putStringSet("my list", itemsList.toSet())
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.popup_no) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            alertDialog.show()
            true // indicate that the long click event is consumed
        }

    }

    override fun onPause() {
        super.onPause()
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        itemsList = ArrayList(sharedPreferences.getStringSet("my list", setOf()))
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemsList)
        listView.adapter = arrayAdapter
    }
}