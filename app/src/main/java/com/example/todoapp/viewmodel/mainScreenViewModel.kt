package com.example.todoapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.database.Todo
import com.example.todoapp.database.TodoDB
import com.example.todoapp.database.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class todoViewModel(application: Application): ViewModel(){
    private val repository: TodoRepository
    var allTodo : LiveData<List<Todo>>

    init {
        val dao = TodoDB.getDatabase(application).getTodoDao()
        repository = TodoRepository(dao)
        allTodo = repository.allTodos
    }

    fun insertTodo(todo: Todo) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(todo)
    }

    fun updateTodo(todo: Todo) = viewModelScope.launch(Dispatchers.IO){
        Log.d("Here for update", todo.toString())
        repository.update(todo)
    }

    fun deleteTodo(todo: Todo) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(todo)
    }
}



class StdVMFactory(val app: Application): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return todoViewModel(app) as T
    }
}