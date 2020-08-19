package com.example.pomodoro2.data

import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.platform.data.IRepository
import com.example.pomodoro2.platform.data.InMemoryDataSource
import com.example.pomodoro2.platform.data.IDbLikeDataSource
import com.example.pomodoro2.platform.functional.Result
import com.example.pomodoro2.platform.functional.Result.Success
import com.example.pomodoro2.platform.functional.Result.Error
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap


/**
 * Concrete implementation to load tasks from the data sources into a cache.
 *
 * To simplify the sample, this repository only uses the local data source only if the remote
 * data source fails. Remote is the source of truth.
 */
class DefaultTaskRepository private constructor(
    private val tasksRemoteDataSource: IDbLikeDataSource<Task>,
    private val tasksLocalDataSource: IDbLikeDataSource<Task>,
    private val inMemoryDataSource: InMemoryDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IRepository<Task/*, BaseSpecification*/> {

    // Domain Aggregate Root data in memory
    private var cachedTasks: ConcurrentHashMap<String, Task>? = null

    /**
     * 我们在Specification里面定义更加复杂的查询条件
     *
     * @param specification 此处举例：基于id批量查询
     * @return
     */
    override suspend fun querySpecification(
        forceUpdate: Boolean
        /*, specification: BaseSpecification*/
    ): Result<List<Task>> {

        return withContext(ioDispatcher) {
            // Respond immediately with cache if available and not dirty
            if (!forceUpdate) {
                cachedTasks?.let { cachedTasks ->
                    return@withContext Success(cachedTasks.values.sortedBy { it.priority })
                }
            }

            // If forceUpdate, retrieving newTasks from remote data source
            val newTasks = fetchTasksFromRemoteOrLocal(forceUpdate)

            // Refresh the cache with the new tasks
            (newTasks as? Success)?.let { refreshCache(it.data) }

            cachedTasks?.values?.let { tasks ->
                return@withContext Success(tasks.sortedBy { it.priority })
            }

            (newTasks as? Success)?.let {
                if (it.data.isEmpty()) {
                    return@withContext Success(it.data)
                }
            }

            return@withContext Error(Exception("Illegal state"))
        }
    }


    // Implement IRepository
    override suspend fun ofId(id: String, forceUpdate: Boolean): Result<Task> {

        return withContext(ioDispatcher) {
            // Respond immediately with cache if available
            if (!forceUpdate) {
                getTaskWithId(id)?.let {
                    return@withContext Success(it)
                }
            }

            val newTask = fetchTaskFromRemoteOrLocal(id, forceUpdate)

            // Refresh the cache with the new tasks
            (newTask as? Success)?.let { cacheTask(it.data) }

            return@withContext newTask
        }
/*
        // Old Implementation w/o local cache
        val localTask = tasksLocalDataSource.retrieveTask(id)
        if (localTask is Result.Success) return localTask
        return Result.Error(Exception("Error fetching from remote and local"))
*/
    }

    override suspend fun add(task: Task) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            coroutineScope {
                launch { tasksRemoteDataSource.createOrUpdateTask(it) }
                launch { tasksLocalDataSource.createOrUpdateTask(it) }
            }
        }
        cachedTasks?.set(task.id, task)
        tasksLocalDataSource.createOrUpdateTask(task)
    }

    override suspend fun remove(task: Task) {
        coroutineScope {
            launch { tasksRemoteDataSource.deleteTask(task) }
            launch { tasksLocalDataSource.deleteTask(task) }
        }

        cachedTasks?.remove(task.id)
    }


    override suspend fun removeAllTask() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { tasksRemoteDataSource.deleteAllTasks() }
                launch { tasksLocalDataSource.deleteAllTasks() }
            }
        }
        cachedTasks?.clear()
    }

    // TODO: remove below fun due to thought: 用集合的思想来操作聚合根
    // How to deal with below update method?
    override suspend fun updateTask(task: Task) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            coroutineScope {
                launch { tasksRemoteDataSource.createOrUpdateTask(it) }
                launch { tasksLocalDataSource.createOrUpdateTask(it) }
            }
        }
    }

    override suspend fun completeTask(task: Task) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            it.isCompleted = true
            coroutineScope {
                launch { tasksRemoteDataSource.completeTask(it) }
                launch { tasksLocalDataSource.completeTask(it) }
            }
        }
    }

    override suspend fun activateTask(task: Task) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            it.isCompleted = false
            coroutineScope {
                launch { tasksRemoteDataSource.activateTask(it) }
                launch { tasksLocalDataSource.activateTask(it) }
            }

        }
    }

    override fun setSelectedTask(task: Task) = inMemoryDataSource.setSelectedTask(task)

    override fun getSelectedTask() = inMemoryDataSource.getSelectedTask()

    override suspend fun initializeStartingTasks() {
        withContext(ioDispatcher) {
            coroutineScope {
                // retrieve template tasks from application side
                val newTasks = tasksLocalDataSource.initializeTutorialTasks()

                // add new tasks into cache and database
                (newTasks as? Success)?.let {
                    for (task in it.data)
                        add(task)
                }
            }
        }
    }


    private suspend fun fetchTaskFromRemoteOrLocal(
        taskId: String,
        forceUpdate: Boolean
    ): Result<Task> {
        // Remote first
        when (val remoteTask = tasksRemoteDataSource.retrieveTask(taskId)) {
            is Error -> println("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteTask.data)
                return remoteTask
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Error(Exception("Refresh failed"))
        }

        // Local if remote fails
        val localTasks = tasksLocalDataSource.retrieveTask(taskId)
        if (localTasks is Success) return localTasks
        return Error(Exception("Error fetching from remote and local"))
    }


    // TODO: add synchronization strategy for repository, plan to following google sample
    private suspend fun fetchTasksFromRemoteOrLocal(forceUpdate: Boolean): Result<List<Task>> {
        // Remote first
        when (val remoteTasks = tasksRemoteDataSource.retrieveTasks()) {
            // TODO: check why Timber can't work?
            is Error -> println("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteTasks.data)
                return remoteTasks
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Result.Error(Exception("Can't force refresh: remote data source is unavailable"))
        }

        // Local if remote fails
        val localTasks = tasksLocalDataSource.retrieveTasks()
        if (localTasks is Success) return localTasks
        return Error(Exception("Error fetching from remote and local"))
    }

    private fun refreshCache(tasks: List<Task>) {
        cachedTasks?.clear()
        tasks.sortedBy { it.id }.forEach {
            cacheAndPerform(it) {}
        }
    }

    private suspend fun refreshLocalDataSource(tasks: List<Task>) {
        tasksLocalDataSource.deleteAllTasks()
        for (task in tasks) {
            tasksLocalDataSource.createOrUpdateTask(task)
        }
    }

    private suspend fun refreshLocalDataSource(task: Task) {
        tasksLocalDataSource.createOrUpdateTask(task)
    }

    private fun getTaskWithId(id: String) = cachedTasks?.get(id)

    private fun cacheTask(task: Task): Task {
        val cachedTask = Task(
            task.id, task.title, task.description, task.isCompleted,
            task.imageId, task.priority, task.createTime
        )
        // Create if it doesn't exist.
        if (cachedTasks == null) {
            cachedTasks = ConcurrentHashMap()
        }
        cachedTasks?.put(cachedTask.id, cachedTask)
        return cachedTask
    }

    private inline fun cacheAndPerform(task: Task, perform: (Task) -> Unit) {
        val cachedTask = cacheTask(task)
        perform(cachedTask)
    }


    companion object {
        /**
         * volatile: make sure the value of INSTANCE is always up-to-date and the same to all execution threads.
         */
        @Volatile
        private lateinit var INSTANCE: DefaultTaskRepository

        fun getInstance(
            tasksRemoteDataSource: IDbLikeDataSource<Task>,
            tasksLocalDataSource: IDbLikeDataSource<Task>,
            inMemoryDataSource: InMemoryDataSource,
            ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        ): DefaultTaskRepository {
            synchronized(DefaultTaskRepository::class.java) {

                // The .isInitialized Kotlin property returns true if the lateinit property
                // (INSTANCE in this example) has been assigned a value, and false otherwise.
                if (!Companion::INSTANCE.isInitialized) {
                    INSTANCE =
                        DefaultTaskRepository(
                            tasksRemoteDataSource,
                            tasksLocalDataSource,
                            inMemoryDataSource,
                            ioDispatcher
                        )
                }
                // Return instance; smart cast to be non-null.
                return INSTANCE
            }
        }

        fun buildInstanceForTesting(
            tasksRemoteDataSource: IDbLikeDataSource<Task>,
            tasksLocalDataSource: IDbLikeDataSource<Task>,
            inMemoryDataSource: InMemoryDataSource,
            ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        ): DefaultTaskRepository =
            DefaultTaskRepository(
                tasksRemoteDataSource,
                tasksLocalDataSource,
                inMemoryDataSource,
                ioDispatcher
            )

    }


}