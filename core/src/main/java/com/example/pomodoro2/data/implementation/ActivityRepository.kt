package com.example.pomodoro2.data.implementation

import com.example.pomodoro2.data.interface_def.ActivityDataSource
import com.example.pomodoro2.data.interface_def.InMemoryDataSource
import com.example.pomodoro2.domain.Activity
import com.example.pomodoro2.domain.Task

class ActivityRepository(
    private val activityDataSource: ActivityDataSource,
    private val inMemoryDataSource: InMemoryDataSource
) {

    suspend fun addActivity(activity: Activity) = activityDataSource.add(activity)

    suspend fun getActivities(task: Task) = activityDataSource.getAll(task)
}