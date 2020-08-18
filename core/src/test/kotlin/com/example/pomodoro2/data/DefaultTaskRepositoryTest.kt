package com.example.pomodoro2.data

import com.example.pomodoro2.BaseUnitTest
import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.platform.data.IDbLikeDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test

class DefaultTaskRepositoryTest: BaseUnitTest() {

    private lateinit var tasksRemoteDataSource: IDbLikeDataSource<Task>
    private lateinit var tasksLocalDataSource: IDbLikeDataSource<Task>

    // Class under test
    private lateinit var tasksRepository: DefaultTaskRepository

    @ExperimentalCoroutinesApi
    @Before
    fun createRepository() {
        tasksRemoteDataSource = FakeTasksDataSource
        tasksLocalDataSource = FakeTasksDataSource
        // Get a reference to the class under test
        tasksRepository = DefaultTaskRepository.getInstance(
            tasksRemoteDataSource, tasksLocalDataSource, AppInMemoryDataSource()
        )

    }

    @After
    fun tearDown() {
    }

    @Test
    fun getTasks() {
    }
}