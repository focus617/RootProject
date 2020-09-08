package com.example.pomodoro2.standAlonePlugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.task
import java.text.SimpleDateFormat
import java.util.*

class StandAlonePlugin : Plugin<Project> {
    companion object DateAndTime {
        const val timeFormat = "MM/dd/yyyy HH:mm:ss.SSS"
        const val dateFormat = "yyyy-MM-dd"
    }

    override fun apply(project: Project) {

        //定义一个 Task
        val testTask = project.task("PluginTestTask") {
            val description = "This is PluginTestTask."

            repeat(4) { counter ->
                project.tasks.register("task$counter") {
                    doLast {
                        println("task number $counter: doLast invoke...")
                    }
                }
            }
            dependsOn("task0")

            // dynamically add dependencies to a task, at runtime.
            project.tasks.named("task0") { dependsOn("task3", "task2", "task1") }

            doFirst {
                println("\nPluginTestTask doFirst invoke...")
                println(description)
            }

            doLast {
                println("\nPluginTestTask doLast invoke...")
            }

        }

        project.task("showProject") {
            doLast {
                println("\nshowProject doLast invoke...")
                println(project.project)
                println("name: " + project.name)
                println("version: " + project.version)
                println("path: " + project.path)
                println("description: " + project.description)
                println("buildDir: " + project.buildDir)

            }
        }

        project.task("showTasks") {

            dependsOn("showProject")

            doLast {
                println("\nshowTasks doLast invoke...")
                println(project.tasks.javaClass)
                println(project.tasks.size)
                //project.tasks.forEach{task -> println(task.name)}

                testTask.description = "change to new description"
                println(testTask.description)
            }
        }

        project.task("showDate") {
            doFirst {
                println("Current date is ${SimpleDateFormat(dateFormat).format(Date())}")
            }
        }

        project.task("showTime") {
            doFirst {
                println("Current time is ${SimpleDateFormat(timeFormat).format(Date())}")
            }
        }

        project.task("getSrcFileName") {
            inputs.dir("src")
            outputs.file("build/test-results/test/info.txt")

            doFirst {
                val srcDir = project.fileTree("src")
                val infotxt = project.file("build/test-results/test/info.txt")
                infotxt.writeText("")

                srcDir.filter { it.isFile }.forEach {
                    infotxt.appendText(it.absolutePath)
                    infotxt.appendText("\r\n")
                }
            }
        }

        lateinit var version: String

        project.task("distribution") {
            doLast {
                println("We build the zip with version = $version")
            }
        }

        project.tasks.register("release") {
            dependsOn("distribution")
            doFirst {
                println("\nrelease doFirst invoke...")
                project.gradle.taskGraph.allTasks.forEach {
                    println("Task: ${it.name}")
                }
            }
            doLast {
                println("\nrelease doLast invoke...")
                println("We release now")
            }
        }

        project.gradle.taskGraph.whenReady {
            version =
                if (hasTask("release")) "1.0"
                else "1.0-SNAPSHOT"
        }

        // Call the build-in API
        project.task("myCopy", Copy::class) {
            val description = "Copies sources to the dest directory"
            val group = "Custom"

            from("src")
            into("dest")
        }


    }
}



