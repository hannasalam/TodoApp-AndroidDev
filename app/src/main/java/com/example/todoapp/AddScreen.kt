package com.example.todoapp

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class AddScreen : AppCompatActivity() {

    private lateinit var editTextTodoTitle: EditText
    private lateinit var buttonSubmit: Button
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_screen)

        editTextTodoTitle = findViewById(R.id.editTextTodoTitle)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        val old = intent.getStringExtra("title")
        var id:Int = -1
        var isDone:Boolean  = false
        if(old!=null){
            editTextTodoTitle.setText(old)
            id = intent.getIntExtra("id",-1)
            Log.d("ID", id.toString())
            isDone = intent.getBooleanExtra("isDone", false)
        }
        buttonSubmit.setOnClickListener {
            // Handle the submission action

            val todoTitle = editTextTodoTitle.text.toString()
            if (todoTitle.isNotEmpty()) {
                val intent = Intent()
                if(id!=-1){
                    intent.putExtra("id", id)
                    intent.putExtra("isDone", isDone)
                }
                intent.putExtra("title", todoTitle)
                setResult( Activity.RESULT_OK, intent)
                finish()

            } else {
                // Inform the user to enter a title
                editTextTodoTitle.error = "Please enter a todo title"
            }
        }
    }
}