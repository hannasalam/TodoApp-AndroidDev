package com.example.todoapp.database

import androidx.lifecycle.LiveData

class TodoRepository(private val todoDao: TodoDao) {

    var allTodos: LiveData<List<Todo>> = todoDao.getAllTodos()

    suspend fun insert(todo: Todo){
        todoDao.insert(todo)
    }

    suspend fun delete(todo: Todo){
        todoDao.delete(todo)
    }

    suspend fun update(todo: Todo){
        todoDao.update(todo.id, todo.title, todo.isDone)
    }
}