package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.local.TodoDB
import com.example.todoapp.domain.repository.TodoRepository
import com.example.todoapp.domain.repository.TodoRepositoryImpl

class DBContainer {
        @Volatile
        private var INSTANCE: TodoDB? = null
        fun getDatabase(context: Context): TodoDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDB::class.java,
                    TodoDB.DB_NAME
                ).build()

                INSTANCE = instance
                instance
            }
        }

        fun provideTodoRepository(database: TodoDB): TodoRepository = TodoRepositoryImpl(
            database.getTodoDao()
        )
}