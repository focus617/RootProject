package com.example.pomodoro2.features.tasks.domain

import com.example.pomodoro2.interactors.*

/**
 * Definition of UseCase Set for each feature
 */
data class TaskInteractors (
    val createNewTaskUseCase: CreateNewTaskUseCase,
    val getTasksUseCase: GetTasksUseCase,
    val removeTask: RemoveTask,
    val removeAllTask: RemoveAllTask,
    val updateTaskUseCase: UpdateTaskUseCase,
    val completeTaskUseCase: CompleteTaskUseCase,
    val activateTaskUseCase: ActivateTaskUseCase,
    val getSelectedTask: GetSelectedTask,
    val setSelectedTask: SetSelectedTask,
    val initStartingTasks: InitializeStartingTasks
) : Interactors