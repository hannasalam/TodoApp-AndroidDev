package com.example.todoapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.todoapp.data.local.TodoDao
import com.example.todoapp.domain.model.Todo

class TodoRepositoryImpl(private val todoDao: TodoDao):TodoRepository {

    override fun getAllTodos(): LiveData<List<Todo>> {
        return todoDao.getAllTodos()
    }

    override suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    override suspend fun delete(todo: Todo) {
        todoDao.delete(todo)
    }

    override suspend fun update(todo: Todo) {
        todoDao.update(todo.id, todo.title, todo.isDone)
    }
}