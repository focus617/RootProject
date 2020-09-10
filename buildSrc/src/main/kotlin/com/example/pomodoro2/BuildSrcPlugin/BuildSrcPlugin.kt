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
        project.task<GreetingTask>("hello"){
            description = "Runs the default greeting task."
            group = "pluginTest"
        }

        // Customize the greeting task
        project.tasks.register<GreetingTask>("greeting") {
            description = "Runs the Customized greeting task."
            group = "pluginTest"

            greeting = "greetings from GreetingTask"
            if(project.hasProperty("Output"))
                println(greeting)
        }

        // The extension object is added to the project with the name 'greeting',
        // which can be configured in build.gradle
        val extension =
            project.extensions.create<GreetingPluginExtension>("greeting")

        // Add a task that uses configuration from the extension object
        project.task("greeting2") {
            description = "Runs the Customized greeting task to consume the extension object."
            group = "pluginTest"

            doLast{
                println("${extension.greeter} saying '${extension.message}.' ")
            }
        }


        project.task<GreetingToFileTask>("greeting3") {
            description = "Runs the Customized greeting task which write to file."
            group = "pluginTest"
            destination = { project.extra["greetingFile"]!! }
        }

        project.task("sayGreeting") {
            dependsOn("greeting3")
            description = "Runs the Customized greeting task which read the file."
            group = "pluginTest"

            doLast {
                var text = project.file(project.extra["greetingFile"]!!).readText()
                println(text)
            }
        }


    }

}

