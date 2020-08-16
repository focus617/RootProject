package com.example.pomodoro2.platform.data

import com.example.pomodoro2.domain.Activity
import com.example.pomodoro2.domain.Task

interface ActivityDataSource {

    suspend fun add(activity: Activity)

    suspend fun getAll(task: Task): List<Activity>

    suspend fun remove(activity: Activity)

    suspend fun removeAll()

//    suspend fun initializeTutorialActivities()

}