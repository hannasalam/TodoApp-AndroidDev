package com.example.todoapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

import com.example.todoapp.di.DBContainer
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.repository.TodoRepository
import com.example.todoapp.domain.repository.TodoRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class todoViewModel(application: Application,private val container: DBContainer): ViewModel(){
    var allTodo : LiveData<List<Todo>>
    private val repository: TodoRepository
    init {
        val dao = container.getDatabase(application)
        repository = container.provideTodoRepository(dao)
        allTodo = repository.getAllTodos()
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



class StdVMFactory(val app: Application, val container: DBContainer): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return todoViewModel(app, container) as T
    }
}