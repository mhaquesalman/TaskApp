package com.example.taskapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskapp.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks_tbl ORDER BY id DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks_tbl WHERE id=:taskId")
    fun getTaskById(taskId: Int): Flow<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks_tbl")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM tasks_tbl WHERE task_title LIKE :searchQry OR task_description LIKE :searchQry")
    fun searchTasks(searchQry: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks_tbl ORDER BY CASE WHEN task_priority LIKE 'L%' " +
            "THEN 1 WHEN task_priority LIKE 'M%' THEN 2 WHEN task_priority LIKE 'H%'" +
            "THEN 3 END")
    fun sortByLowPriority(): Flow<List<Task>>

    @Query("SELECT * FROM tasks_tbl ORDER BY CASE WHEN task_priority LIKE 'H%' " +
            "THEN 1 WHEN task_priority LIKE 'M%' THEN 2 WHEN task_priority LIKE 'L%'" +
            "THEN 3 END")
    fun sortByHighPriority(): Flow<List<Task>>
}