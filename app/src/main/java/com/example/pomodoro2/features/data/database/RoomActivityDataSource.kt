package com.example.pomodoro2.features.data.database

import android.content.Context
import com.example.pomodoro2.platform.data.ActivityDataSource
import com.example.pomodoro2.domain.model.Action
import com.example.pomodoro2.domain.model.Task
import com.example.pomodoro2.framework.extension.asDatabaseEntity

class RoomActivityDataSource(val context: Context) :
    ActivityDataSource {

    private val activityDao = AppDatabase.getInstance(context.applicationContext).actionDao

    override suspend fun add(action: Action){
        activityDao.insertAction(action.asDatabaseEntity())
    }

    override suspend fun getAll(task: Task): List<Action> = activityDao.getAction(task.id).asDomainModel()

    override suspend fun remove(action: Action) {
        activityDao.removeAction(action.asDatabaseEntity())
    }

    override suspend fun removeAll() {
        activityDao.clearActionTable()
    }


}