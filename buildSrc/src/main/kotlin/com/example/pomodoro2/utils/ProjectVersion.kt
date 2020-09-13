package com.example.pomodoro2.utils

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.task
import java.util.*

data class ProjectVersion(
    var major: Int,
    var minor: Int,
    var release: Boolean = false
) {

    @Override
    override fun toString() = "$major.$minor${if (release) "" else "-SNAPSHOT"}"
}

/**
 * 将版本信息存储在外部文件中,并配置构建脚本来读取它
 */
open class LoadVersionTask : DefaultTask() {
    init {
        group = "versioning"
        description = "Retrieve the project version for property file."
    }

    @Input
    var version: ProjectVersion = ProjectVersion(0, 1)

    fun readVersion(): ProjectVersion {
        val versionFileName = project.extra["versionFile"].toString()
        logger.quiet("Reading from version file: $versionFileName")

        val versionFile = project.file(versionFileName)
        if (!versionFile.exists()) {
            throw GradleException(
                "Required version file does nto exist:" +
                        versionFile.canonicalPath
            )
        } else {
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
    fun loadVersion() {
        logger.quiet("\n$name: loadVersion()")
        version = readVersion()
        logger.quiet("Retrieved version is '$version'")
    }
}

