package com.example.pomodoro2.platform.data

import com.example.pomodoro2.domain.model.Action
import com.example.pomodoro2.domain.model.Task

interface InMemoryDataSource {

    fun setSelectedTask(task: Task)

    fun getSelectedTask(): Task

    fun setSelectedActivity(action: Action)

    fun getSelectedActivity(): Action?

}