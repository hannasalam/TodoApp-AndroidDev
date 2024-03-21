package com.example.todoapp.domain.usecases

import com.example.todoapp.domain.model.Todo
import com.example.todoapp.data.local.TodoRepositoryImpl

class GetTodoByIdUseCase(val repository: TodoRepositoryImpl
) {
    fun execute(id:Int): Todo? {
        return repository.getById(id)
    }
}