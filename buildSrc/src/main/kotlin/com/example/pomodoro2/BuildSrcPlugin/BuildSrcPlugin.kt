package com.example.pomodoro2.buildSrcPlugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.task


class BuildSrcPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        // Use the default greeting
        project.task<GreetingTask>("hello")

        // Customize the greeting
        project.tasks.register<GreetingTask>("greeting") {
            greeting = "greetings from GreetingTask"
        }
    }

}

