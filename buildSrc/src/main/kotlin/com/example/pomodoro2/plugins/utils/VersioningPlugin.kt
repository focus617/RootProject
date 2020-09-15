package com.example.pomodoro2.plugins.utils

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.task
import java.io.File

class VersioningPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        // separate capabilities from conventions
        project.plugins.apply(BasePlugin::class)

        lateinit var versionFile: File

        // actual task
        project.task<LoadVersionTask>("loadVersion") {
            // actual task provide value for the exposed properties of custom task
            // to configure the behavior
            versionFile = getVersionFile()
            project.version = readVersion()
        }

        project.task("printVersion") {
            group = "versioning"
            description = "Prints project version."
            //dependsOn("loadVersion")

            doLast {
                logger.quiet("\n$name: doLast()")
                val projectVersion = project.version as ProjectVersion
                logger.quiet("Version: $projectVersion")
            }
        }

        project.task<ReleaseVersionTask>("makeReleaseVersion") {
            finalizedBy("printVersion")
            release = (project.version as ProjectVersion).prodReady
            destFile = versionFile
        }
    }


}