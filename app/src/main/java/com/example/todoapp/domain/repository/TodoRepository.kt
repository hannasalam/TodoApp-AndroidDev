package com.example.todoapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.todoapp.domain.model.Todo

interface TodoRepository {
    fun getAllTodos(): LiveData<List<Todo>>
    suspend fun insert(todo: Todo)
    suspend fun delete(todo: Todo)
    suspend fun update(todo: Todo)

}