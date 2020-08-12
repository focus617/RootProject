package com.example.pomodoro2.features.infra.database

import android.content.Context
import com.example.pomodoro2.data.TaskDataSource
import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.framework.extension.asDatabaseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomTaskDataSource(val context: Context) : TaskDataSource {

    private val taskDao = AppDatabase.getInstance(context.applicationContext).taskDao

    override suspend fun add(task: Task){
        withContext(Dispatchers.IO) {
            taskDao.insertTask(task.asDatabaseEntity())
        }
    }

    override suspend fun getAll(): List<Task> {
        return taskDao.getAllTasksLive().value!!.asDomainModel()
    }

    override suspend fun remove(task: Task){
        withContext(Dispatchers.IO) {
            taskDao.removeTask(task.asDatabaseEntity())
        }
    }

}