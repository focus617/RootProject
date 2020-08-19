package com.example.pomodoro2.data

import com.example.pomodoro2.BaseUnitTest
import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.platform.data.IDbLikeDataSource
import com.example.pomodoro2.platform.functional.Result.Success
import com.example.pomodoro2.platform.functional.Result.Error
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class DefaultTaskRepositoryTestSpecialCase: BaseUnitTest() {

    @ExperimentalCoroutinesApi
    @Test
    fun getTasks_emptyRepositoryAndUninitializedCache() = runBlockingTest {
        val emptySource = FakeDataSource()
        val tasksRepository = DefaultTaskRepository.getInstance(
            emptySource, emptySource, AppInMemoryDataSource(), Dispatchers.Unconfined
        )

        assertThat(tasksRepository.querySpecification() is Success).isTrue()
    }
}

class DefaultTaskRepositoryTest: BaseUnitTest() {

    private val task1 = Task( title = "title1", description = "Description1", isCompleted = false, imageId = 1, priority = 1)
    private val task2 = Task( title = "title2", description = "Description2", isCompleted = true, imageId = 2, priority = 2)
    private val task3 = Task( title = "title3", description = "Description3", isCompleted = true, imageId = 3, priority = 3)
    private val newTask = Task( title = "Title new", description = "Description new", isCompleted = false, imageId = 1, priority = 4)

    private val remoteTasks = listOf(task1, task2).sortedBy { it.priority }
    private val localTasks = listOf(task3).sortedBy { it.priority }
    private val newTasks = listOf(task3).sortedBy { it.priority }

    private lateinit var tasksRemoteDataSource: IDbLikeDataSource<Task>
    private lateinit var tasksLocalDataSource: IDbLikeDataSource<Task>

    // Class under test
    private lateinit var tasksRepository: DefaultTaskRepository

    @ExperimentalCoroutinesApi
    @Before
    fun createRepository() {
        tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        tasksLocalDataSource = FakeDataSource(localTasks.toMutableList())
        // Get a reference to the class under test
        tasksRepository = DefaultTaskRepository.getInstance(
            tasksRemoteDataSource, tasksLocalDataSource, AppInMemoryDataSource()
        )

    }

    @After
    fun tearDown() {
    }

    @Test
    fun getTasks_repositoryCachesAfterFirstApiCall() = runBlockingTest {
        // Trigger the repository to load data, which loads from remote and caches
        val initial = tasksRepository.querySpecification()

        (tasksRemoteDataSource as FakeDataSource).tasks = newTasks.toMutableList()

        val second = tasksRepository.querySpecification()

        // Initial and second should match because we didn't force a refresh
        assertThat(second).isEqualTo(initial)
    }


}