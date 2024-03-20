package com.example.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.concurrent.Volatile

@Database(entities = [Todo::class], version = 1)
abstract class TodoDB: RoomDatabase(){
    abstract fun getTodoDao(): TodoDao
    companion object{

        @Volatile
        private var INSTANCE: TodoDB? = null
        fun getDatabase(context: Context): TodoDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDB::class.java,
                    "todo_table"
                ).build()

                INSTANCE = instance
                instance
            }
        }

    }
}