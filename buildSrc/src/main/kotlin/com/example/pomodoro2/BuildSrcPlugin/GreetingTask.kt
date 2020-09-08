package com.example.pomodoro2.buildSrcPlugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class GreetingTask : DefaultTask() {
    @get:Input
    var greeting = "hello from GreetingTask"

    @TaskAction
    fun greet() {
        println(greeting)
    }
}
