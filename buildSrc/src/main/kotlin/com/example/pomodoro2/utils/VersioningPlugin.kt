package com.example.pomodoro2.utils

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.delegateClosureOf
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.task
import java.io.File

class VersioningPlugin : Plugin<Project> {

    private var projectVersion: ProjectVersion? = null

    override fun apply(project: Project) {
        // separate capabilities from conventions
        project.plugins.apply(BasePlugin::class)

        lateinit var versionFile: File

        // actual task
        project.task<LoadVersionTask>("loadVersion") {
            // actual task provide value for the exposed properties of custom task
            // to configure the behavior

            logger.quiet("\n>> configure task: $name")
            versionFile = checkVersionFile()
            projectVersion = readVersion(versionFile)
        }

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

        project.task("makeReleaseVersion") {
            group = "versioning"
            description = "Makes project a release version."
            finalizedBy("printVersion")

            inputs.property("release", projectVersion!!.release)
            outputs.file(versionFile)

            doLast {
                logger.quiet("\n$name: doLast()")
                projectVersion?.release = true

                // How to modify properties file?
                //ant.propertyfile(file: versionFile) {
                //    entry(key: 'release', type: 'string', operation: '=', value: 'true')
            }
        }
    }


}