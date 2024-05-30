package com.example.taskapp.repositories

import com.example.taskapp.models.Task
import com.example.taskapp.room.TaskDao
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    val getAllTasks: Flow<List<Task>> = taskDao.getAllTasks()
    val sortByLowPriority: Flow<List<Task>> = taskDao.sortByLowPriority()
    val sortByHighPriority: Flow<List<Task>> = taskDao.sortByHighPriority()

    fun getTaskById(taskId: Int): Flow<Task> = taskDao.getTaskById(taskId)

    fun searchTasks(searchQry: String): Flow<List<Task>> = taskDao.searchTasks(searchQry)

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun deleteAllTasks() = taskDao.deleteAllTasks()
}