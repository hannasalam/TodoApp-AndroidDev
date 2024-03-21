package com.example.todoapp

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.example.todoapp.di.DBInjector
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.presentation.StdVMFactory
import com.example.todoapp.presentation.addScreenStdVMFactory
import com.example.todoapp.presentation.addScreenViewModel
import com.example.todoapp.presentation.todoViewModel


class AddScreen : AppCompatActivity() {

    private lateinit var vModel: addScreenViewModel
    private lateinit var editTextTodoTitle: EditText
    private lateinit var buttonSubmit: Button
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_screen)

        val dbContainer = DBInjector.dbContainer
        vModel = ViewModelProvider(
            this,
            addScreenStdVMFactory(this.applicationContext as Application, dbContainer)
        )[addScreenViewModel::class.java]


        editTextTodoTitle = findViewById(R.id.editTextTodoTitle)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        val id = intent.getIntExtra("id",-1)
        var currentTodo: Todo? = null
        Log.d("Current todo", id.toString())
        if(id!=-1){
            currentTodo = vModel.getTodoById(id)
            Log.d("Found", vModel.getTodoById(id).toString())
            editTextTodoTitle.setText(currentTodo?.title)

        }
        buttonSubmit.setOnClickListener {
            // Handle the submission action

            val todoTitle = editTextTodoTitle.text.toString()
            if (todoTitle.isNotEmpty()) {
                if(id!=-1){
                    if (currentTodo != null) {
                        vModel.updateTodo(currentTodo.copy(title = todoTitle))
                    }
                }
                else{
                    vModel.insertTodo(Todo(title = todoTitle))
                }
                finish()

            } else {
                // Inform the user to enter a title
                editTextTodoTitle.error = "Please enter a todo title"
            }
        }
    }
}