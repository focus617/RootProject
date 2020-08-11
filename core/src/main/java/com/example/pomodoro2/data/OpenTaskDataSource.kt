package com.example.pomodoro2.data

import com.example.pomodoro2.domain.Task

interface OpenTaskDataSource {

    fun setOpenTask(task: Task)

    fun getOpenTask(): Task
}