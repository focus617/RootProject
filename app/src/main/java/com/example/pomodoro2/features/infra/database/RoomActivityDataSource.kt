package com.example.pomodoro2.features.infra.database

import android.content.Context
import com.example.pomodoro2.R
import com.example.pomodoro2.data.ActivityDataSource
import com.example.pomodoro2.data.TaskDataSource
import com.example.pomodoro2.domain.Activity
import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.features.infra.database.TaskConstants.images
import com.example.pomodoro2.features.infra.database.TaskConstants.titles
import com.example.pomodoro2.framework.extension.asDatabaseEntity

class RoomActivityDataSource(val context: Context) : ActivityDataSource {

    private val activityDao = AppDatabase.getInstance(context.applicationContext).activityDao

    override suspend fun add(activity: Activity){
        activityDao.insertActivity(activity.asDatabaseEntity())
    }

    override suspend fun getAll(task: Task): List<Activity> = activityDao.getActivities(task.id).asDomainModel()

    override suspend fun remove(activity: Activity) {
        activityDao.removeActivity(activity.asDatabaseEntity())
    }

    override suspend fun removeAll() {
        activityDao.clearActivityTable()
    }


}