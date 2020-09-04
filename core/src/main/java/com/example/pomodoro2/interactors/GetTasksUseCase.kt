package com.example.pomodoro2.interactors

import com.example.pomodoro2.domain.model.Task
import com.example.pomodoro2.interactors.TasksFilterType.*
import com.example.pomodoro2.domain.model.ITaskRepository
import com.example.pomodoro2.platform.functional.Result
import com.example.pomodoro2.platform.functional.Result.Success

class GetTasksUseCase(
    private val taskRepository: ITaskRepository<Task>
) {

    suspend operator fun invoke(
        forceUpdate: Boolean = false,
        currentFiltering: TasksFilterType = ALL_TASKS
    ):Result<List<Task>> {

        val tasksResult = taskRepository.querySpecification()

        // Filter tasks
        if (tasksResult is Success && currentFiltering != ALL_TASKS) {
            val tasks = tasksResult.data

            val tasksToShow = mutableListOf<Task>()
            // We filter the tasks based on the requestType
            for (task in tasks) {
                when (currentFiltering) {
                    ACTIVE_TASKS -> if (task.isActive) {
                        tasksToShow.add(task)
                    }
                    COMPLETED_TASKS -> if (!task.isActive) {
                        tasksToShow.add(task)
                    }
                    else -> NotImplementedError()
                }
            }
            return Success(tasksToShow)
        }
        return  tasksResult
    }
}

/**
 * Used with the filter spinner in the tasks list.
 */
enum class TasksFilterType {
    /**
     * Do not filter tasks.
     */
    ALL_TASKS,

    /**
     * Filters only the active (not completed yet) tasks.
     */
    ACTIVE_TASKS,

    /**
     * Filters only the completed tasks.
     */
    COMPLETED_TASKS
}