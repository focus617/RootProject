package com.example.pomodoro2.platform.data

import com.example.pomodoro2.domain.Activity
import com.example.pomodoro2.domain.Task

interface InMemoryDataSource {

    fun setSelectedTask(task: Task)

    fun getSelectedTask(): Task

    fun setSelectedActivity(activity: Activity)

    fun getSelectedActivity(): Activity?

}