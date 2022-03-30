package com.example.pomodoro2.plugins.utils

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Zip
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.task

class UtilsPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.task("today") {
            group = "utils"
            doFirst {
                println("Today is ${DateAndTime().today()}")
            }
        }

        project.task("currentTime") {
            group = "utils"
            doFirst {
                println("Current time is ${DateAndTime().getSystemTime()}")
            }
        }

        //定义一个 Task
        val initTask = project.task<DefaultTask>("InitTask") {
            description = "This is InitTask."
            group = "pluginTest"

            // dynamically create task, at runtime.
            repeat(4) { counter ->
                project.tasks.register<DefaultTask>("task$counter") {
                    group = "pluginTest"

                    doLast {
                        println("task number $counter: doLast invoke...")
                    }
                }
            }
            dependsOn("task0")
            finalizedBy("task3")
            shouldRunAfter("showProject")

            // dynamically add dependencies to a task, at runtime.
            project.tasks.named("task0") {
                dependsOn("task1", "task2")
            }
            project.tasks.named("task1"){
                mustRunAfter("task2")
            }

            doFirst {
                println("\n$name doFirst invoke...")
                println(description)
            }

            doLast {
                println("\n$name doLast invoke...")
            }

        }

        project.task("showProject") {
            group = "pluginTest"

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
            group = "pluginTest"
            dependsOn("showProject")

            doLast {
                println("\nshowTasks doLast invoke...")
                println(project.tasks.javaClass)
                println(project.tasks.size)
                //project.tasks.forEach{task -> println(task.name)}

                initTask.description = "change to new description"
                println(initTask.description)
            }
        }

        // Incremental Builds
        project.task("getSrcFileName") {
            group = "pluginTest"

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
            group = "pluginTest"

            doLast {
                println("We build the zip with version = $version")
            }
        }

        project.tasks.register("release") {
            group = "pluginTest"
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


        //for including in the copy task
        val dataContent = project.copySpec {
            from("src/data")
            include("*.data")
        }

        // Demonstrates the build-in 'typed task' declarations
        project.task("initConfig", Copy::class) {
            group = "pluginTest"

            val description = "Copies sources to the dest directory"
            val group = "Custom"

            from("src") {
                include("*.kts" )
                into("copytest")
            }

            from("src/main/languages") {
                rename("EN_US_(.*)", "$1")
            }

            into("build/test-results")
            exclude("**/*.bak")
            includeEmptyDirs = false
            with(dataContent)

        }

        project.task("myZipCopy", Zip::class) {
            group = "pluginTest"

            val description = "Copies sources to the dest directory"
            val group = "Custom"

            archiveBaseName.set("kotlin")
            destinationDirectory.set(project.file("build/test-results"))

            from("src")
            {
                include("*.kts" )
                into("copytest")
            }

        }
/*

        project.task("callJava"){
        group = "pluginTest"

            project.javaexec {
                main = "HelloWorld"
                classpath(".")
            }
        }

*/

    }
}



