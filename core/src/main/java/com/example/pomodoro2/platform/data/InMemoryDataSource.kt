package com.example.pomodoro2.platform.data

import com.example.pomodoro2.domain.model.Activity
import com.example.pomodoro2.domain.model.Task

interface InMemoryDataSource {

    fun setSelectedTask(task: Task)

    fun getSelectedTask(): Task

    fun setSelectedActivity(activity: Activity)

    fun getSelectedActivity(): Activity?

}