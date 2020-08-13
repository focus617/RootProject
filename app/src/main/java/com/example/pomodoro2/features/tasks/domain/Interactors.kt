package com.example.pomodoro2.features.tasks.domain

import com.example.pomodoro2.interactors.*

/**
 * Definition of UseCase Set for each feature
 */
data class Interactors(
    val addTask: AddTask,
    val removeTask: RemoveTask,
    val getTasksUseCase: GetTasksUseCase,
    val removeAllTask: RemoveAllTask,
    val getSelectedTask: GetSelectedTask,
    val setSelectedTask: SetSelectedTask,
    val initStartingTasks: InitializeStartingTasks
)