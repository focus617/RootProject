package com.example.pomodoro2.plugin

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

class GreetingTaskTest {

    @Test
    fun canAddTaskToProject() {
        val project = ProjectBuilder.builder().build()
        //val task = project.task('greeting', typeOf<GreetingTask>())
    }
}