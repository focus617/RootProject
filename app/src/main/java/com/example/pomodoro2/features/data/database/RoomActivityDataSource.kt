package com.example.pomodoro2.features.data.database

import android.content.Context
import com.example.pomodoro2.platform.interface_def.ActivityDataSource
import com.example.pomodoro2.domain.Activity
import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.framework.extension.asDatabaseEntity

class RoomActivityDataSource(val context: Context) :
    ActivityDataSource {

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