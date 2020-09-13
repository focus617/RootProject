package com.example.pomodoro2.utils

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.task
import java.util.*

data class ProjectVersion(
    var major: Int,
    var minor: Int,
    var release: Boolean = false
    ) {

    @Override
    override fun toString() = "$major.$minor${if(release)"" else "-SNAPSHOT"}"
}


open class LoadVersionTask : DefaultTask() {
    init {
        group = "pluginTest"
        description = "Retrieve the project version."
    }

    @Input
    var version: ProjectVersion = ProjectVersion(0,1)

    private fun readVersion(): ProjectVersion{
        logger.quiet("Reading the version file")

        val versionFile = project.file("version.properties")
        if (!versionFile.exists()) {
            throw GradleException("Required version file does nto exist:" +
                    versionFile.canonicalPath
            )
        }
        else {
            val properties = Properties()
            properties.load(versionFile.inputStream())
            
            return ProjectVersion(
                properties.getProperty("major").toInt(),
                properties.getProperty("minor").toInt(),
                properties.getProperty("release").toBoolean()
            )
        }
    }

    @TaskAction
    fun loadVersion(){
        logger.quiet("\n$name: loadVersion()")
        version = readVersion()
        logger.quiet("Retrieved version is '$version'")
    }
}

class VersionsPlugin : Plugin<Project> {

    lateinit var projectVersion: ProjectVersion

    override fun apply(project: Project) {
        // separate capabilities from conventions
        project.plugins.apply(BasePlugin::class)

        // actual task
        project.task<LoadVersionTask>("loadVersion"){
            // actual task provide value for the exposed properties of custom task
            // to configure the behavior

            doLast{
                logger.quiet("\n$name: doLast()")
                projectVersion = version
            }
        }
    }

}