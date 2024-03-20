package com.example.todoapp.database

import android.icu.text.CaseMap.Title
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    var title: String,
    @ColumnInfo(defaultValue = false.toString()) var isDone: Boolean = false
)
