package com.example.pomodoro2.buildSrcPlugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * the custom task class that encapsulates the behavior of your logic
 */
open class GreetingTask : DefaultTask() {
    init {
        group = "pluginTest"
        description = "Runs the custom greeting task."
    }

    @get:Input
    var greeting = "hello from GreetingTask"

    @TaskAction // Annotation declares a method to be executed
    open fun greet() {
        println(greeting)
    }
}
