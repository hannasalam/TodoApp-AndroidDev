package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.local.TodoDB
import com.example.todoapp.data.local.TodoRepositoryImpl

class DBContainer {
        @Volatile
        private var INSTANCE: TodoDB? = null
        fun getDatabase(context: Context): TodoDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDB::class.java,
                    TodoDB.DB_NAME
                ).allowMainThreadQueries().build()

                INSTANCE = instance
                instance
            }
        }


        fun provideTodoRepository(database: TodoDB): TodoRepositoryImpl = TodoRepositoryImpl.getInstance(
            database.getTodoDao()
        )
}