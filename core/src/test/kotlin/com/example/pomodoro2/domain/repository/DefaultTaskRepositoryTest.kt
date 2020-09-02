package com.example.pomodoro2.domain.repository

import com.example.pomodoro2.BaseUnitTest
import com.example.pomodoro2.data.AppInMemoryDataSource
import com.example.pomodoro2.data.FakeDataSource
import com.example.pomodoro2.domain.model.Task
import com.example.pomodoro2.platform.functional.Result.Success
import com.example.pomodoro2.platform.functional.Result.Error
import com.google.common.truth.Truth.assertThat
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.*
import org.amshove.kluent.`should not contain`
import org.amshove.kluent.shouldNotContain
import org.junit.After
import org.junit.Before
import org.junit.Test

class DefaultTaskRepositorySpecialCaseTest : BaseUnitTest() {

    @ExperimentalCoroutinesApi
    @Test
    fun querySpecification_emptyRepositoryAndUninitializedCache() = runBlocking {
        val emptySource = FakeDataSource()
        val tasksRepository = DefaultTaskRepository.buildInstanceForTesting(
            emptySource, emptySource,
            AppInMemoryDataSource(), Dispatchers.Unconfined
        )

        assertThat(tasksRepository.querySpecification() is Success).isTrue()
    }
}

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */
@ExperimentalCoroutinesApi
class DefaultTaskRepositoryTest : BaseUnitTest() {

    private val task1 = Task(
        title = "title1",
        description = "Description1",
        isCompleted = false,
        imageId = 1,
        priority = 1
    )
    private val task2 = Task(
        title = "title2",
        description = "Description2",
        isCompleted = true,
        imageId = 2,
        priority = 2,
        parent = task1
    )
    private val task3 = Task(
        title = "title3",
        description = "Description3",
        isCompleted = true,
        imageId = 3,
        priority = 3
    )
    private val newTask = Task(
        title = "Title new",
        description = "Description new",
        isCompleted = false,
        imageId = 1,
        priority = 4
    )

    private val remoteTasks = listOf(task1, task2).sortedBy { it.priority }
    private val localTasks = listOf(task3).sortedBy { it.priority }
    private val newTasks = listOf(task3).sortedBy { it.priority }

    private lateinit var tasksRemoteDataSource: FakeDataSource
    private lateinit var tasksLocalDataSource: FakeDataSource

    // Class under test
    private lateinit var tasksRepository: DefaultTaskRepository

    @ExperimentalCoroutinesApi
    @Before
    fun createRepository() {
        tasksRemoteDataSource =
            FakeDataSource(remoteTasks.toMutableList())
        tasksLocalDataSource =
            FakeDataSource(localTasks.toMutableList())
        // Get a reference to the class under test
        tasksRepository = DefaultTaskRepository.buildInstanceForTesting(
            tasksRemoteDataSource,
            tasksLocalDataSource,
            AppInMemoryDataSource()
        )

    }

    @After
    fun tearDown() {
    }

    /**
     * Test Suite for fun querySpecification
     */
    @Test
    fun querySpecification_refreshesLocalDataSource() = runBlocking {
        val initialLocal = tasksLocalDataSource.tasks!!.toList()

        // When tasks are requested first time from the tasks repository
        val newTasks = (tasksRepository.querySpecification() as Success).data

        // Then tasks are loaded from the remote data source
        assertThat(newTasks).isEqualTo(remoteTasks)
        assertThat(newTasks).isEqualTo(tasksLocalDataSource.tasks)
        assertThat(tasksLocalDataSource.tasks).isNotEqualTo(initialLocal)
    }

    @Test
    fun querySpecification_repositoryCachesAfterFirstApiCall() = runBlocking {

        // Trigger the repository to load data, which loads from remote and caches
        val initial = tasksRepository.querySpecification()

        tasksRemoteDataSource.tasks = newTasks.toMutableList()

        val second = tasksRepository.querySpecification()

        // Initial and second should match because we didn't force a refresh
        assertThat(second).isEqualTo(initial)
    }

    @Test
    fun querySpecification_forceRefresh() = runBlocking {
        // Trigger the repository to load data, which loads from remote and caches
        val initial = tasksRepository.querySpecification()

        tasksRemoteDataSource.tasks = newTasks.toMutableList()

        val second = tasksRepository.querySpecification(forceUpdate = true)

        // Initial and second should not match after forcing a refresh
        assertThat(second).isNotEqualTo(initial)

        // Then tasks are loaded from the remote data source
        assertThat((second as Success).data).isEqualTo(newTasks)
    }

    @Test
    fun querySpecification_WithDirtyCache_tasksAreRetrievedFromRemote() = runBlocking {
        // First call returns from REMOTE
        val tasks = tasksRepository.querySpecification()

        // Set a different list of tasks in REMOTE
        tasksRemoteDataSource.tasks = newTasks.toMutableList()

        // But if tasks are cached, subsequent calls load from cache
        val cachedTasks = tasksRepository.querySpecification()
        assertThat(cachedTasks).isEqualTo(tasks)

        // Now force remote loading
        val refreshedTasks = tasksRepository.querySpecification(true) as Success

        // Tasks must be the recently updated in REMOTE
        assertThat(refreshedTasks.data).isEqualTo(newTasks)
    }

    @Test
    fun querySpecification_WithDirtyCache_remoteUnavailable_error() = runBlocking {
        // Make remote data source unavailable
        tasksRemoteDataSource.tasks = null

        // Load tasks forcing remote load
        val refreshedTasks = tasksRepository.querySpecification(true)

        // Result should be an error
        assertThat(refreshedTasks).isInstanceOf(Error::class.java)
    }

    @Test
    fun querySpecification_WithRemoteDataSourceUnavailable_tasksAreRetrievedFromLocal() =
        runBlocking {
            // When the remote data source is unavailable
            tasksRemoteDataSource.tasks = null

            // The repository fetches from the local source
            assertThat((tasksRepository.querySpecification() as Success).data).isEqualTo(localTasks)
        }

    @Test
    fun querySpecification_WithBothDataSourcesUnavailable_returnsError() = runBlocking {
        // When both sources are unavailable
        tasksRemoteDataSource.tasks = null
        tasksLocalDataSource.tasks = null

        // The repository returns an error
        assertThat(tasksRepository.querySpecification()).isInstanceOf(Error::class.java)
    }

    /**
     * Test Suite for fun selectBy
     */
    @Test
    fun `selectBy_return correct answer with empty Spec filter`() = runBlocking {
        // Trigger the repository to load data, which loads from remote and caches
        val initial = tasksRepository.querySpecification()


        val taskBySelected = tasksRepository.selectBy(null)

        // both task1 and task2 should be selected
        assertThat(taskBySelected).contains(task1)
        assertThat(taskBySelected).contains(task2)

    }

    @Test
    fun `selectBy_return correct children by parent Spec filter`() = runBlocking {
        // Trigger the repository to load data, which loads from remote and caches
        val initial = tasksRepository.querySpecification()


        val taskBySelected = tasksRepository.selectBy(listOf(ChildTaskSpec(task1)))

        // Then task1 should not be selected
        assertThat(taskBySelected).doesNotContain(task1)

        // task2 should be selected via ChildTaskSpec
        assertThat(taskBySelected).contains(task2)
    }

    /**
     * Test Suite for fun ofId
     */
    @Test
    fun ofId_repositoryCachesAfterFirstApiCall() = runBlocking {
        // Trigger the repository to load data, which loads from remote
        tasksRemoteDataSource.tasks = mutableListOf(task1)
        val task1FirstTime = tasksRepository.ofId(task1.id) as Success

        // check basic retrieving from remote
        assertThat(task1FirstTime.data.id).isEqualTo(task1.id)

        // Configure the remote data source to store a different task
        tasksRemoteDataSource.tasks = mutableListOf(task2)

        val task1SecondTime = tasksRepository.ofId(task1.id) as Success
        val task2SecondTime = tasksRepository.ofId(task2.id) as Success

        // Both work because one is in remote and the other in cache
        assertThat(task1SecondTime.data.id).isEqualTo(task1.id)
        assertThat(task2SecondTime.data.id).isEqualTo(task2.id)
    }

    @Test
    fun ofId_forceRefresh() = runBlocking {
        // Trigger the repository to load data, which loads from remote and caches
        tasksRemoteDataSource.tasks = mutableListOf(task1)
        tasksRepository.ofId(task1.id)

        // Configure the remote data source to return a different task
        tasksRemoteDataSource.tasks = mutableListOf(task2)

        // Force refresh
        val task1SecondTime = tasksRepository.ofId(task1.id, true)
        val task2SecondTime = tasksRepository.ofId(task2.id, true)

        // Only task2 works because the cache and local were invalidated
        assertThat((task1SecondTime as? Success)?.data?.id).isNull()
        assertThat((task2SecondTime as? Success)?.data?.id).isEqualTo(task2.id)
    }

    /**
     * Test Suite for fun add
     */
    @Test
    fun `add_new data_update to cache, local and remote all`() = runBlocking {
        // Make sure newTask is not in the remote or local datasources or cache
        assertThat(tasksRemoteDataSource.tasks).doesNotContain(newTask)
        assertThat(tasksLocalDataSource.tasks).doesNotContain(newTask)
        assertThat((tasksRepository.querySpecification() as? Success)?.data).doesNotContain(newTask)

        // When a task is saved to the tasks repository
        tasksRepository.add(newTask)

        // Then the remote and local sources are called and the cache is updated
        assertThat(tasksRemoteDataSource.tasks).contains(newTask)
        assertThat(tasksLocalDataSource.tasks).contains(newTask)

        val result = tasksRepository.querySpecification() as? Success
        assertThat(result?.data).contains(newTask)
    }

    @Test
    fun `add_when both remote and local failed_cache still alive`() = runBlocking {
        // Save a task
        tasksRepository.add(newTask)

        // Verify it's in the cache
        tasksLocalDataSource.deleteAllTasks() // Make sure they don't come from local
        tasksRemoteDataSource.deleteAllTasks() // Make sure they don't come from remote
        val result = tasksRepository.querySpecification() as Success
        assertThat(result.data).contains(newTask)
    }

    /**
     * Test Suite for fun remove
     */
    @Test
    fun `remove_data_are really deleted at local and remote`() = runBlocking {

        val initialTasks = (tasksRepository.querySpecification() as? Success)?.data
        // Ensure that data item exist before testing
        assertThat(initialTasks).contains(task1)

        // Delete this data item
        tasksRepository.remove(task1)

        // Fetch data again
        val afterDeleteTasks = (tasksRepository.querySpecification() as? Success)?.data

        // Verify only one task was deleted
        assertThat(afterDeleteTasks?.size).isEqualTo(initialTasks!!.size - 1)
        assertThat(afterDeleteTasks).doesNotContain(task1)

        // Then the remote and local sources are all updated
        assertThat(tasksRemoteDataSource.tasks).doesNotContain(task1)
        assertThat(tasksLocalDataSource.tasks).doesNotContain(task1)

    }

    /**
     * Test Suite for fun removeAll
     */
    @Test
    fun `removeAll_all tasks are deleted`() = runBlocking {
        val initialTasks = (tasksRepository.querySpecification() as? Success)?.data

        // Delete all tasks
        tasksRepository.removeAll()

        // Fetch data again
        val afterDeleteTasks = (tasksRepository.querySpecification() as? Success)?.data

        // Verify tasks are empty now
        assertThat(initialTasks).isNotEmpty()
        assertThat(afterDeleteTasks).isEmpty()
    }

    /**
     * Test Suite for fun completeTask
     */
    @Test
    fun completeTask_completesTaskToServiceAPIUpdatesCache() = runBlocking {
        // Save a task
        tasksRepository.add(newTask)

        // Make sure it's active
        assertThat((tasksRepository.ofId(newTask.id) as Success).data.isCompleted).isFalse()

        // Mark is as complete
        tasksRepository.completeTask(newTask)

        // Verify it's now completed
        assertThat((tasksRepository.ofId(newTask.id) as Success).data.isCompleted).isTrue()
    }

    /**
     * Test Suite for fun activateTask
     */
    @Test
    fun activateTask_activeTaskToServiceAPIUpdatesCache() = runBlocking {
        // Save a task
        tasksRepository.add(newTask)
        tasksRepository.completeTask(newTask)

        // Make sure it's completed
        assertThat((tasksRepository.ofId(newTask.id) as Success).data.isActive).isFalse()

        // Mark is as active
        tasksRepository.activateTask(newTask)

        // Verify it's now activated
        val result = tasksRepository.ofId(newTask.id) as Success
        assertThat(result.data.isActive).isTrue()
    }

    /**
     * Test Suite for fun set/get SelectedTask
     */
    @Test
    fun `selectedTask_set then get selected task to ServiceAPI updates cache`() = runBlocking {
        //Set one task as selected task
        tasksRepository.setSelectedTask(newTask)

        // Verify it's now selected in cache
        val returnTask = tasksRepository.getSelectedTask()
        assertThat(returnTask).isEqualTo(newTask)
    }

    /**
     * Test Suite for fun initializeStartingTasks
     */
    @Test
    fun `initializeStartingTasks_really add template tasks`()= runBlocking {
        // Delete all tasks
        tasksRepository.removeAll()

        // make sure that cache is empty
        val initialTasks = (tasksRepository.querySpecification() as? Success)?.data
        assertThat(initialTasks).isEmpty()

        tasksRepository.initializeStartingTasks()

        // Fetch data again
        val afterInitTasks = (tasksRepository.querySpecification() as? Success)?.data

        // Verify tasks are empty now
        assertThat(afterInitTasks).isNotEmpty()
    }



}