package com.example.pomodoro2.plugins.buildSrcPlugin

import com.example.pomodoro2.buildSrcPlugin.GreetingTask
import com.example.pomodoro2.plugins.base.BasePluginTest
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class BuildSrcPluginTest : BasePluginTest() {

    @Before
    fun init(){
        buildFile.writeText("""
            plugins {
                id("com.focus617.plugins.BuildSrcPlugin")
            }
        """.trimIndent())
    }

    @Test
    fun `BuildSrcPlugin_can Add GreetingTask to Project`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.focus617.plugins.BuildSrcPlugin")
        assertTrue(project.tasks.getByName("hello") is GreetingTask)
    }

/*    @Test
    fun `BuildSrcPlugin_can run Task hello`() {
        val result = runTask("hello")

        assertEquals(TaskOutcome.SUCCESS, result.task(":hello")?.outcome)
        assertTrue(result.output.contains("hello inherent from GreetingTask"))
    }

    @Test
    fun `BuildSrcPlugin_can run Task greeting`() {

        val result = runTask("greeting")

        assertEquals(TaskOutcome.SUCCESS, result.task(":greeting")?.outcome)
        assertTrue(result.output.contains("greetings from GreetingTask"))
    }

    @Test
    fun `BuildSrcPlugin_can run Task greeting with extension`() {
        // Configure the extension using a DSL block
        buildFile.appendText("""
        configure<com.example.pomodoro2.plugins.buildSrcPlugin.GreetingPluginExtension> {
            message = "Hello from GreetingPlugin"
            greeter = "Gradle"
        }
        """.trimIndent())

        val result = runTask("greetingWithExtension")

        assertEquals(TaskOutcome.SUCCESS, result.task(":greetingWithExtension")?.outcome)
        assertTrue(result.output.contains("Gradle"))
        assertTrue(result.output.contains("Hello from GreetingPlugin"))
    }*/
}