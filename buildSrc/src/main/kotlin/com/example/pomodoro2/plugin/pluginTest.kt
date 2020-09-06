package com.example.pomodoro2.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.text.SimpleDateFormat
import java.util.*

class PluginTest : Plugin<Project> {
    companion object DateAndTime {
        const val timeFormat = "MM/dd/yyyy HH:mm:ss.SSS"
        const val dateFormat = "yyyy-MM-dd"
    }

    override fun apply(project: Project) {

        //定义一个 Task
        val testTask = project.task("PluginTestTask") {
            val description = "This is PluginTestTask."

            doFirst {
                println("PluginTestTask doFirst invoke...")
                println(description)
            }
            doLast{
                println("PluginTestTask doLast invoke...")
            }

        }

        project.task("showProject") {
            doLast {
                println("showProject doLast invoke...")
                println("project:"+project.project)
                println("name:"+project.name)
                println("version:"+project.version)
                println("path:"+project.path)
                println("description:"+project.description)
                println("buildDir:"+project.buildDir)

            }
        }

        project.task("showTasks") {
            doLast {
                println("showTasks doLast invoke...")
                println(project.tasks.javaClass)
                println(project.tasks.size)
                //project.tasks.forEach{task -> println(task.name)}

                testTask.description = "change to new description"
                println(testTask.description)

            }
        }.dependsOn("PluginTestTask")

        project.task("showDate"){
            doFirst{
                println("Current date is ${SimpleDateFormat(dateFormat).format(Date()) }")
            }
        }

        project.task("showTime"){
            doFirst{
                println("Current time is ${SimpleDateFormat(timeFormat).format(Date()) }")
            }
        }

        project.task("getSrcFileName"){
            inputs.dir("src")
            //outputs.file("info.txt")

            doFirst{
                val srcDir = project.fileTree("src")
                val infotxt = project.file("info.txt")
                infotxt.writeText("")

                srcDir.forEach{
                    if(it.isFile)
                        infotxt.appendText(it.absolutePath)
                        infotxt.appendText("\r\n")
                }
            }
        }
    }
}

