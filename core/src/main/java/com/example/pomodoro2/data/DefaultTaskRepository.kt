package com.example.pomodoro2.data

import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.platform.data.IRepository
import com.example.pomodoro2.platform.data.InMemoryDataSource
import com.example.pomodoro2.platform.data.TaskDataSource
import com.example.pomodoro2.platform.functional.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.ConcurrentMap


/**
 * Concrete implementation to load tasks from the data sources into a cache.
 *
 * To simplify the sample, this repository only uses the local data source only if the remote
 * data source fails. Remote is the source of truth.
 */
class DefaultTaskRepository private constructor(
    private val tasksLocalDataSource: TaskDataSource,
    private val inMemoryDataSource: InMemoryDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): IRepository<Task/*, BaseSpecification*/> {

    private var cachedTasks: ConcurrentMap<String, Task>? = null

    // TODO: add synchronization strategy for repository, plan to following google sample
    private suspend fun fetchTasksFromRemoteOrLocal(forceUpdate: Boolean): Result<List<Task>> {
    // TODO: before implement remote, need to study how to make a fake remote data source.
/*
        // Remote first
        val remoteTasks = tasksRemoteDataSource.getTasks()
        when (remoteTasks) {
            is Error -> Timber.w("Remote data source fetch failed")
            is Result.Success -> {
                refreshLocalDataSource(remoteTasks.data)
                return remoteTasks
            }
            else -> throw IllegalStateException()
        }
*/
        // Don't read from local if it's forced
        if (forceUpdate) {
            return Result.Error(Exception("Can't force refresh: remote data source is unavailable"))
        }

        // Local if remote fails
        val localTasks = tasksLocalDataSource.getTasks()
        if (localTasks is Result.Success) return localTasks
        return Result.Error(Exception("Error fetching from remote and local"))
    }

    // Implement IRepository
    override suspend fun ofId(id: Long, forceUpdate: Boolean): Result<Task> {
        val localTask = tasksLocalDataSource.getTask(id)
        if (localTask is Result.Success) return localTask
        return Result.Error(Exception("Error fetching from remote and local"))
    }

    override suspend fun add(task: Task) {
        if(task != null) {
            cachedTasks?.set(task.id.toString(), task)
            tasksLocalDataSource.createTask(task)
        }
    }

    override suspend fun remove(task: Task) {
        if(task != null) {
            cachedTasks?.remove(task.id.toString(), task)
            tasksLocalDataSource.deleteTask(task)
        }
    }

    /**
     * 我们在Specification里面定义更加复杂的查询条件
     *
     * @param specification 此处举例：基于id批量查询
     * @return
     */
    override suspend fun querySpecification(forceUpdate: Boolean/*specification: BaseSpecification*/): Result<List<Task>>{
/*
        val tasks: MutableList<Task> = ArrayList()
        if (specification !is CustomerSpecification) {
            return Result.Success(tasks)
        }
        if (CollectionUtils.isEmpty(specification.getIds())) {
            return tasks
        }
        specification.getIds().forEach { id ->
            if (ofId(id) != null) {
                tasks.add(ofId(id))
            }
        }
        return Result.Success(tasks)
*/
        return tasksLocalDataSource.getTasks()
    }

    // TODO: remove below fun due to thought: 用集合的思想来操作聚合根
    // How to deal with below update method?
    override suspend fun updateTask(task: Task) = tasksLocalDataSource.updateTask(task)


    override suspend fun removeAllTask() = tasksLocalDataSource.deleteAllTasks()

    override suspend fun completeTask(task: Task) = tasksLocalDataSource.completeTask(task)

    override suspend fun activateTask(task: Task) = tasksLocalDataSource.activateTask(task)

    override fun setSelectedTask(task: Task) = inMemoryDataSource.setSelectedTask(task)

    override fun getSelectedTask() = inMemoryDataSource.getSelectedTask()

    override suspend fun initializeStartingTasks() = tasksLocalDataSource.initializeTutorialTasks()

    companion object {
        /**
         * volatile: make sure the value of INSTANCE is always up-to-date and the same to all execution threads.
         */
        @Volatile
        private lateinit var INSTANCE: DefaultTaskRepository

        fun getInstance(
            tasksLocalDataSource: TaskDataSource,
            inMemoryDataSource: InMemoryDataSource
        ): DefaultTaskRepository {
            synchronized(DefaultTaskRepository::class.java) {

                // The .isInitialized Kotlin property returns true if the lateinit property
                // (INSTANCE in this example) has been assigned a value, and false otherwise.
                if (!Companion::INSTANCE.isInitialized) {
                    INSTANCE =
                        DefaultTaskRepository(
                            tasksLocalDataSource,
                            inMemoryDataSource
                        )
                }
                // Return instance; smart cast to be non-null.
                return INSTANCE
            }
        }

    }
}