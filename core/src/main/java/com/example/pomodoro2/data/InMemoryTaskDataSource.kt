package com.example.pomodoro2.data

import com.example.pomodoro2.domain.Task

interface InMemoryTaskDataSource {

    fun setSelectedTask(task: Task)

    fun getSelectedTask(): Task

}