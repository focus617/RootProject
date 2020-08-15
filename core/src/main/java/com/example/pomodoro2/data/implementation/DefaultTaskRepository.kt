package com.example.pomodoro2.data.implementation

import com.example.pomodoro2.data.interface_def.InMemoryDataSource
import com.example.pomodoro2.data.interface_def.TaskDataSource
import com.example.pomodoro2.data.interface_def.TaskRepositoryInterface
import com.example.pomodoro2.domain.Task

class DefaultTaskRepository private constructor(
    private val taskDataSource: TaskDataSource,
    private val inMemoryDataSource: InMemoryDataSource
): TaskRepositoryInterface {

    override suspend fun createTask(task: Task) = taskDataSource.createTask(task)

    override suspend fun getTasks() = taskDataSource.getTasks()

    override suspend fun updateTask(task: Task) = taskDataSource.updateTask(task)

    override suspend fun removeTask(task: Task) = taskDataSource.deleteTask(task)

    override suspend fun removeAllTask() = taskDataSource.deleteAllTasks()

    override suspend fun completeTask(task: Task) = taskDataSource.completeTask(task)

    override suspend fun activateTask(task: Task) = taskDataSource.activateTask(task)

    override fun setSelectedTask(task: Task) = inMemoryDataSource.setSelectedTask(task)

    override fun getSelectedTask() = inMemoryDataSource.getSelectedTask()

    override suspend fun initializeStartingTasks() = taskDataSource.initializeTutorialTasks()

    companion object {
        /**
         * volatile: make sure the value of INSTANCE is always up-to-date and the same to all execution threads.
         */
        @Volatile
        private lateinit var INSTANCE: DefaultTaskRepository

        fun getInstance(
            taskDataSource: TaskDataSource,
            inMemoryDataSource: InMemoryDataSource
        ): DefaultTaskRepository {
            synchronized(DefaultTaskRepository::class.java) {

                // The .isInitialized Kotlin property returns true if the lateinit property
                // (INSTANCE in this example) has been assigned a value, and false otherwise.
                if (!Companion::INSTANCE.isInitialized) {
                    INSTANCE =
                        DefaultTaskRepository(
                            taskDataSource,
                            inMemoryDataSource
                        )
                }
                // Return instance; smart cast to be non-null.
                return INSTANCE
            }
        }

    }
}