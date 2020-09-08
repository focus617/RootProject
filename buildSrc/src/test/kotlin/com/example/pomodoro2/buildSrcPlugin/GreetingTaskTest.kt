package com.example.pomodoro2.buildSrcPlugin

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File


class BuildSrcPluginTest {

    @Rule @JvmField
    val testProjectDir: TemporaryFolder = TemporaryFolder()
    private lateinit var project: Project

    private lateinit var settingsFile: File
    private lateinit var buildFile: File


    @Before
    fun setup(){
        settingsFile = testProjectDir.newFile("settings.gradle.kts")
        buildFile = testProjectDir.newFile("build.gradle.kts")
    }

    @Test
    fun `BuildSrcPlugin_can Add GreetingTask to Project`() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.focus617.BuildSrcPlugin")
        assertTrue(project.tasks.getByName("hello") is GreetingTask)
    }

    @Test
    fun `BuildSrcPlugin_can run Task`() {
        settingsFile.writeText("""
            rootProject.name = "hello-world"
        """.trimIndent())

        buildFile.writeText("""
            tasks.register("helloWorld") {
                doLast {
                    println("Hello world!")
                }
            }
        """.trimIndent())

        project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.focus617.BuildSrcPlugin")

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("hello")
            .build()

        assertTrue(result.output.contains("Hello world!"))
        assertEquals(TaskOutcome.SUCCESS, result.task(":helloWorld")?.outcome)
    }
}