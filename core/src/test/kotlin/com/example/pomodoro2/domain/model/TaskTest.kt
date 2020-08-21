package com.example.pomodoro2.domain.model

import com.example.pomodoro2.BaseUnitTest
import com.example.pomodoro2.interactors.GetTasksUseCase
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for [Task].
 */
class TaskTest: BaseUnitTest() {

    @Test
    fun `Task_Logger can work`(){
        val task = Task(
            title = "测试任务",
            description = "desc1",
            isCompleted = false,
            imageId = 1,
            priority = 1
        )
        task.testLogger()
        assertTrue(true)
    }

}