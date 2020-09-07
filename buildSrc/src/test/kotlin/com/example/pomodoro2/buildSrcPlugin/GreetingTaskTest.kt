package com.example.pomodoro2.buildSrcPlugin

import com.example.pomodoro2.buildSrcPlugin.GreetingTask
import org.gradle.kotlin.dsl.create
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import kotlin.test.assertTrue

class GreetingTaskTest {

    @Test
    fun canAddTaskToProject() {
        val project = ProjectBuilder.builder().build()
        val task = project.tasks.create<GreetingTask>("greeting")
        assertTrue(task is GreetingTask)
    }
}