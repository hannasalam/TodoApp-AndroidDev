package com.example.todoapp.domain.usecases

import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.repository.TodoRepositoryImpl

class UpdateTodoUseCase(
    private val repository: TodoRepositoryImpl
) {
    suspend fun execute(todo: Todo){
        repository.update(todo)
    }
}