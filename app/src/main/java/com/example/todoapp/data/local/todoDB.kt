package com.example.todoapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.domain.model.Todo
import kotlin.concurrent.Volatile

@Database(entities = [Todo::class], version = 1)
abstract class TodoDB: RoomDatabase(){
    abstract fun getTodoDao(): TodoDao
    companion object{
        const val DB_NAME = "todo_table"
    }
}