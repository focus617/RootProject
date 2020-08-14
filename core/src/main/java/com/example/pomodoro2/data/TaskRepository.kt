package com.example.pomodoro2.data

import com.example.pomodoro2.domain.Task

class TaskRepository private constructor(
    private val taskDataSource: TaskDataSource,
    private val inMemoryDataSource: InMemoryDataSource
) {

    suspend fun createTask(task: Task) = taskDataSource.createTask(task)

    suspend fun getTasks() = taskDataSource.getTasks()

    suspend fun updateTask(task: Task) = taskDataSource.updateTask(task)

    suspend fun removeTask(task: Task) = taskDataSource.deleteTask(task)

    suspend fun removeAllTask() = taskDataSource.deleteAllTasks()

    fun setSelectedTask(task: Task) = inMemoryDataSource.setSelectedTask(task)

    fun getSelectedTask() = inMemoryDataSource.getSelectedTask()

    suspend fun initializeStartingTasks() = taskDataSource.initializeTutorialTasks()

    companion object {
        /**
         * volatile: make sure the value of INSTANCE is always up-to-date and the same to all execution threads.
         */
        @Volatile
        private lateinit var INSTANCE: TaskRepository

        fun getInstance(
            taskDataSource: TaskDataSource,
            inMemoryDataSource: InMemoryDataSource
        ): TaskRepository {
            synchronized(TaskRepository::class.java) {

                // The .isInitialized Kotlin property returns true if the lateinit property
                // (INSTANCE in this example) has been assigned a value, and false otherwise.
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = TaskRepository(taskDataSource, inMemoryDataSource)
                }
                // Return instance; smart cast to be non-null.
                return INSTANCE
            }
        }

    }
}