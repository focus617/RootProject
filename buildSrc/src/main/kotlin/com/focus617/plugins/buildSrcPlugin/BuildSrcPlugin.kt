package com.focus617.plugins.buildSrcPlugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.*

// A custom plugin extension
open class GreetingPluginExtension {
    var message: String? = null
    var greeter: String? = null
    var callFunc: (String)->Unit = {}
}

class BuildSrcPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        // separate capabilities from conventions
        project.plugins.apply(BasePlugin::class)

        // actual task
        project.task<GreetingTask>("hello"){
            // actual task provide value for the exposed properties of custom task
            // to configure the behavior
            greeting = "hello inherent from GreetingTask"
        }

        // Actual task with Customized behavior of custom greeting task
        project.tasks.register<GreetingTask>("greeting") {
            description = "Runs the Customized greeting task."
            greeting = "greetings from GreetingTask"

            if(project.hasProperty("Output"))
                println(description)
        }

        // The extension object is added to the project with the name 'greeting',
        // which can be configured in build.gradle
        val extension =
            project.extensions.create<GreetingPluginExtension>("greeting")

        // Add a task that uses configuration from the extension object
        project.task("greetingWithExtension") {
            description = "Runs the Customized greeting task to consume the extension object."
            group = "pluginTest"

            doFirst(){
                println("${extension.greeter} saying '${extension.message}.' ")
            }

            doLast{
                extension.callFunc("Called by greetingWithExtension.doLast()")
            }
        }


        project.task<GreetingToFileTask>("greetingForSaveToFile") {
            description = "Runs the Customized greeting task which write to file."
            group = "pluginTest"
            destination = { project.extra["greetingFile"]!! }
        }

        project.task("greetingForDumpFile") {
            dependsOn("greetingForSaveToFile")
            description = "Runs the Customized greeting task which read the file."
            group = "pluginTest"

            doLast {
                val text = project.file(project.extra["greetingFile"]!!).readText()
                println(text)
            }
        }


    }

}

