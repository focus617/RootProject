package com.example.pomodoro2.buildSrcPlugin

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertTrue
import org.junit.Test


class BuildSrcPluginTest {

    @Test
    fun `BuildSrcPlugin_can Add GreetingTask to Project`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.focus617.BuildSrcPlugin")

        assertTrue(project.tasks.getByName("hello") is GreetingTask)
    }
}