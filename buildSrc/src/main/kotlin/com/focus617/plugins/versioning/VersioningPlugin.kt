package com.example.pomodoro2.plugins.versioning

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.task

// A custom plugin extension
const val EXTENSION_NAME = "ProjectVersion"

open class VersioningPluginExtension {
    var versionFile: String? = null
    var callFunc: (String)->Unit = {}
}

class VersioningPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        // separate capabilities from conventions
        project.plugins.apply(BasePlugin::class)

        /**
         * The extension object is added to the project with the name [EXTENSION_NAME],
         * which can be configured in build.gradle
         */
        val extension =
            project.extensions.create<VersioningPluginExtension>(EXTENSION_NAME)

        lateinit var versionFile: VersionFile

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
                val projectVersion = project.version as ProjectVersion
                logger.quiet("Version: $projectVersion")
            }
        }

        project.task<ReleaseVersionTask>("makeReleaseVersion") {
            finalizedBy("printVersion")
            release = (project.version as ProjectVersion).prodReady
            destFile = versionFile
        }

        project.task("createNewMinorRelease"){
            group = "versioning"
            description = "Update project release with increased minor number."
            finalizedBy("printVersion")

            doLast{
                val version = project.version as ProjectVersion
                version.newMinorRelease()
                versionFile.updateVersion(version)
            }

        }
    }


}