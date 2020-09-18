package com.example.pomodoro2.platform.data

import com.example.pomodoro2.domain.model.Action
import com.example.pomodoro2.domain.model.Task

interface ActivityDataSource {

    suspend fun add(action: Action)

    suspend fun getAll(task: Task): List<Action>

    suspend fun remove(action: Action)

    suspend fun removeAll()

//    suspend fun initializeTutorialActivities()

}