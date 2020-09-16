package com.example.pomodoro2.domain.model

import com.example.pomodoro2.BaseUnitTest
import com.example.pomodoro2.interactors.GetTasksUseCase
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [Task].
 */
class TaskTest: BaseUnitTest() {
    lateinit var task: Task

    @Before
    fun setup(){
        task = Task(
            title = "测试任务",
            description = "desc1",
            isCompleted = false,
            imageId = 1,
            priority = 1
        )
    }

    @Test
    fun `Task_Logger can work`(){

        task.testLogger()
        assertTrue(true)
    }

    @Test
    fun `Task_complete makes task inactive`() {
        task.complete()

        assertFalse(task.isActive)
    }

}