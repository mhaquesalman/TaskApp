package com.example.taskapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskapp.utils.Constants
import java.time.Instant
import java.util.Date

@Entity(tableName = Constants.TABLE_NAME)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = Constants.TASK_TITLE)
    val title: String,
    @ColumnInfo(name = Constants.TASK_DESCRIPTION)
    val description: String,
    @ColumnInfo(name = Constants.TASK_PRIORITY)
    val priority: Priority,
    @ColumnInfo(name = Constants.TASK_COMPLETE)
    val isCompleted: Boolean = false,
    @ColumnInfo(name = Constants.TASK_ADD_DATE)
    val entryDate: Date = Date.from(Instant.now())
)