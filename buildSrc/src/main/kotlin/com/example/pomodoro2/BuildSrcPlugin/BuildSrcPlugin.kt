package com.example.pomodoro2.buildSrcPlugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.task

// A custom plugin extension
open class GreetingPluginExtension {
    var message: String? = null
    var greeter: String? = null
}

class BuildSrcPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // separate capabilities from conventions
        project.plugins.apply(BasePlugin::class)

        // Use the default greeting task
        project.task<GreetingTask>("hello")

        // Customize the greeting task
        project.tasks.register<GreetingTask>("greeting") {
            greeting = "greetings from GreetingTask"
        }

        // Add the 'greeting' extension object
        val extension = project.extensions.create<GreetingPluginExtension>("greeting")

        // Add a task that uses configuration from the extension object
        project.task("greeting2") {
            doFirst {
                println(extension.message)
            }

            doLast{
                println("${extension.message} from ${extension.greeter}")
            }
        }
    }

}

