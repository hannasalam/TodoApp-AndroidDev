package com.example.todoapp.domain.usecases

import androidx.lifecycle.LiveData
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.repository.TodoRepositoryImpl

class GetAllTodoUseCase(
    val repository: TodoRepositoryImpl
) {
    fun execute(): LiveData<List<Todo>>{
         return repository.getAllTodos()
    }
}