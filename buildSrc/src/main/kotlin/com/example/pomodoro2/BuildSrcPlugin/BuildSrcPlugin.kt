package com.example.pomodoro2.buildSrcPlugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

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

        // The extension object is added to the project with the name 'greeting',
        // which can be configured in build.gradle
        val extension = project.extensions.create<GreetingPluginExtension>("greeting")

        // Add a task that uses configuration from the extension object
        project.task("greeting2") {
            doLast{
                println("${extension.greeter} saying '${extension.message}.' ")
            }
        }


        project.tasks.register<GreetingToFileTask>("greeting3") {
            destination = { project.extra["greetingFile"]!! }
        }

        project.tasks.register("sayGreeting") {
            dependsOn("greeting3")

            doLast {
                var text = project.file(project.extra["greetingFile"]!!).readText()
                println(text)
            }
        }


    }

}

