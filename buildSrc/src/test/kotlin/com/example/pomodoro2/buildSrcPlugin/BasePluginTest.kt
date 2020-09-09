package com.example.pomodoro2.buildSrcPlugin

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File


abstract class BasePluginTest {

    @Rule @JvmField
    val testProjectDir: TemporaryFolder = TemporaryFolder()
    protected lateinit var settingsFile: File
    protected lateinit var buildFile: File

    @Before
    fun setup(){
        settingsFile = testProjectDir.newFile("settings.gradle.kts")
        settingsFile.writeText("""
            rootProject.name = "test"
        """.trimIndent())

        buildFile = testProjectDir.newFile("build.gradle.kts")
    }


    fun runTask(task: String): BuildResult {
        return GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments(task, "--stacktrace")
            .withPluginClasspath()
            .build()
    }

    fun runTaskWithFailure(task: String): BuildResult {
        return GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments(task, "--stacktrace")
            .withPluginClasspath()
            .buildAndFail()
    }
}