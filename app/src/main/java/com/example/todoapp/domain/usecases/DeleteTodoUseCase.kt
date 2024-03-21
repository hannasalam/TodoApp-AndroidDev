package com.example.todoapp.domain.usecases

import com.example.todoapp.domain.model.Todo
import com.example.todoapp.data.local.TodoRepositoryImpl

class DeleteTodoUseCase (
    private val repository: TodoRepositoryImpl
) {
    suspend fun execute(todo: Todo){
        repository.delete(todo)
    }
}