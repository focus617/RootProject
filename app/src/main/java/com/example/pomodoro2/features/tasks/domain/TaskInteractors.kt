package com.example.pomodoro2.features.tasks.domain

import com.example.pomodoro2.interactors.*
import com.example.pomodoro2.platform.interactor.Interactors

/**
 * Definition of UseCase Set for Task
 *
 * Use Case of Task
 * 1. Adding a subTask to a currently selected task.
 * 2. Removing a subTask from a currently selected task.
 * 3. Getting all activities for currently selected task.
 * 4. Complete the selected subTask
 * 5. Change the title of selected subTask
 *
 */
data class TaskInteractors (
    val createNewTaskUseCase: CreateNewTaskUseCase,
    val getTasksUseCase: GetTasksUseCase,
    val removeTaskUseCase: RemoveTaskUseCase,
    val removeAllTaskUseCase: RemoveAllTaskUseCase,
    val updateTaskUseCase: UpdateTaskUseCase,
    val completeTaskUseCase: CompleteTaskUseCase,
    val activateTaskUseCase: ActivateTaskUseCase,
    val getSelectedTaskUseCase: GetSelectedTaskUseCase,
    val setSelectedTaskUseCase: SetSelectedTaskUseCase,
    val initStartingTasksUseCase: InitializeStartingTasksUseCase
) : Interactors