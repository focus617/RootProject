package com.focus617.plugins.base

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
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

class BuildLogicFunctionalTest : BasePluginTest() {

    @Before
    fun init(){
        buildFile.writeText("""
            tasks.register("helloWorld") {
                doLast {
                    println("Hello world!")
                }
            }
        """.trimIndent())
    }

    @Test
    fun `BasePlugin_Test basic task functionality`(){
        //val result = runTask("helloWorld")
        //assertEquals(TaskOutcome.SUCCESS, result.task(":helloWorld")?.outcome)
        //assertTrue(result.output.contains("Hello world!"))
    }
}