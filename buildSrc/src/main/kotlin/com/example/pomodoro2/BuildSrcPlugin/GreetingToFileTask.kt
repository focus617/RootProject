package com.example.pomodoro2.buildSrcPlugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Working with files in custom tasks and plugins
 * file:///D:/Tools/gradle-6.6.1/docs/userguide/custom_plugins.html#sec:working_with_files_in_custom_tasks_and_plugins
 *
 * Usage: configure [destination] in advance, for example
 * plugin:  task.destination = { project.extra["greetingFile"]!! }
 * in DSL:  extra["greetingFile"] = "$buildDir/hello.txt"
 */
open class GreetingToFileTask : DefaultTask() {

    var destination: Any? = null

    @OutputFile
    fun getDestination(): File {
        return project.file(destination!!)
    }

    @TaskAction
    fun greet() {
        val file = getDestination()
        file.parentFile.mkdirs()
        file.writeText("Hello!")
    }
}

