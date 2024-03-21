package com.example.todoapp.domain.usecases

import androidx.lifecycle.LiveData
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.repository.TodoRepositoryImpl

class GetTodoByIdUseCase(val repository: TodoRepositoryImpl
) {
    fun execute(id:Int): Todo? {
        return repository.getById(id)
    }
}