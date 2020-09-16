package com.example.pomodoro2.plugins.utils

import com.example.pomodoro2.plugins.base.BasePluginTest
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UtilsPluginTest: BasePluginTest() {

    @Before
    fun init(){
        buildFile.writeText("""
            plugins {
                id("com.focus617.plugins.UtilsPlugin")
            }
        """.trimIndent())
    }

    @Test
    fun `UtilsPlugin_has 'today' and 'currentTime' tasks`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.focus617.plugins.UtilsPlugin")

        assertNotNull(project.tasks.getByName("today") )
        assertNotNull(project.tasks.getByName("currentTime") )
    }

/*     @Test
    fun `UtilsPlugin_task 'today' show correct date format`(){
        val result = runTask("today")

        assertEquals(TaskOutcome.SUCCESS, result.task(":today")?.outcome)
        assertTrue(result.output.contains("Today is 2020-"))
    }

   @Test
    fun `UtilsPlugin_task 'currentTime' show correct time format`(){
        val result = runTask("currentTime")

        assertEquals(TaskOutcome.SUCCESS, result.task(":currentTime")?.outcome)
        assertTrue(result.output.contains("Current time is 2020-"))
    }*/
}