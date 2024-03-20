package com.example.todoapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("SELECT * from todo_table order by id ASC")
    fun getAllTodos(): LiveData<List<Todo>>

    @Query("UPDATE todo_table set title = :title, isDone = :isDone where id = :id")
    suspend fun update(id: Int?, title: String, isDone: Boolean)

}