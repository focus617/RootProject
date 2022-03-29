package com.focus617.plugins.buildSrcPlugin

import com.focus617.plugins.base.BasePluginTest
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BuildSrcPluginTest : BasePluginTest() {

    @Before
    fun init() {
        buildFile.writeText(
            """
            plugins {
                id("com.focus617.plugins.BuildSrcPlugin")
            }
            """.trimIndent()
        )
    }

    @Test
    fun `BuildSrcPlugin_can Add GreetingTask to Project`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.focus617.plugins.BuildSrcPlugin")
        assertTrue(project.tasks.getByName("hello") is GreetingTask)
    }
}