package com.example.pomodoro2.features.tasks.domain

import com.example.pomodoro2.interactors.*

data class Interactors(
    val addTask: AddTask,
    val removeTask: RemoveTask,
    val getTasks: GetTasks,
    val removeAllTask: RemoveAllTask,
    val getSelectedTask: GetSelectedTask,
    val setSelectedTask: SetSelectedTask
)