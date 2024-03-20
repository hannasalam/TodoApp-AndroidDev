package com.example.todoapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    var title: String,
    @ColumnInfo(defaultValue = false.toString()) var isDone: Boolean = false
)
