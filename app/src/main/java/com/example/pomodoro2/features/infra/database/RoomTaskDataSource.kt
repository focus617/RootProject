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
        taskDao.insertTask(task.asDatabaseEntity())
    }

    override suspend fun getAll(): List<Task> = taskDao.getTasks().asDomainModel()

    override suspend fun remove(task: Task){
        taskDao.removeTask(task.asDatabaseEntity())
    }

    override suspend fun removeAll() {
        taskDao.clearTaskTable()
    }


}