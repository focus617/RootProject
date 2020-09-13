package com.example.pomodoro2.utils

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.task

class VersioningPlugin : Plugin<Project> {

    private var projectVersion: ProjectVersion? = null

    override fun apply(project: Project) {
        // separate capabilities from conventions
        project.plugins.apply(BasePlugin::class)

        // general task
        project.task("printVersion") {
            group = "versioning"
            description = "Prints project version."

            doLast {
                logger.quiet("\n$name: doLast()")
                if (projectVersion != null)
                    logger.quiet("Version: $projectVersion")
                else
                    logger.quiet("Version: hasn't been defined.")
            }
        }

        // actual task
        project.task<LoadVersionTask>("loadVersion") {
            // actual task provide value for the exposed properties of custom task
            // to configure the behavior

            logger.quiet("\n>> configure task: $name")
            projectVersion = readVersion()
        }
    }

}