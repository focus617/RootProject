package com.example.pomodoro2.buildSrcPlugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.*

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
    @Input
    var password: String? = "Default"

    @OutputFile
    fun getDestination(): File {
        return project.file(destination!!)
    }

    @TaskAction
    fun greet() {
        logger.quiet("GreetingToFileTask: greet()")
        val file = getDestination()
        file.parentFile.mkdirs()
        file.writeText("Hello!")
    }



    private fun getPassword(){
        logger.quiet("getPassword(): reading the private properties file")

        val passwordFile = project.file("private.properties")
        if (passwordFile.exists()) {
            val properties = Properties()
            properties.load(passwordFile.inputStream())
            password = properties.getProperty("release.password")
        }
    }

    @TaskAction
    fun printPassword(){
        logger.quiet("GreetingToFileTask: printPassword()")
        getPassword()
        logger.quiet("Retrieved password is '$password'")
    }
}

