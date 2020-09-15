package com.example.pomodoro2.plugins.utils

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.extra
import java.io.File
import java.util.*

data class ProjectVersion(
    var major: Int,
    var minor: Int,
    var prodReady: Boolean = false
) {

    @Override
    override fun toString() = "$major.$minor${if (prodReady) "" else "-SNAPSHOT"}"

}

/**
 * 将版本信息存储在外部文件中,并配置构建脚本来读取它
 */
open class LoadVersionTask : DefaultTask() {
    @Input
    var property: String = "versionFile"

    @InputFile
    private lateinit var versionFile: File
    fun getVersionFile() = versionFile

    init {
        group = "versioning"
        description = "Retrieve the project version for property file."

        logger.quiet("\n>> configure task: $name")
        checkVersionFile()
//        if (checkVersionFile())
//            project.version = readVersion()
    }

    private fun checkVersionFile(): Boolean {

        logger.quiet("$name: retrieve version file name from property:  '$property'")
        val versionFileName = project.extra[property].toString()

        if (versionFileName == "") {
            throw GradleException(
                "Required version extension doesn't exist: ${project.extra[property]}"
            )
        }
        versionFile = project.file(versionFileName)
        if (!versionFile.exists()) {
            throw GradleException(
                "Required version file doesn't exist:" + versionFile.canonicalPath
            )
        }
        logger.quiet("$name: version file '$versionFileName' exist.")
        return true
    }

    fun readVersion(): ProjectVersion {
        if (!versionFile.exists()) {
            throw GradleException(
                "Required version file does nto exist:" +
                        versionFile.canonicalPath
            )
        } else {
            val versionProps = Properties()
            versionProps.load(versionFile.inputStream())

            return ProjectVersion(
                versionProps.getProperty("major").toInt(),
                versionProps.getProperty("minor").toInt(),
                versionProps.getProperty("release")!!.toBoolean()
            )
        }
    }


    @TaskAction
    fun loadVersion() {
        logger.quiet("\n$name: loadVersion()")
        if (checkVersionFile())
            project.version = readVersion()
        logger.quiet("Retrieved version is '${project.version}'")
    }
}


open class ReleaseVersionTask : DefaultTask() {
    @Input
    var release: Boolean = false

    @OutputFile
    lateinit var destFile: File

    init {
        group = "versioning"
        description = "Makes project a release version."
    }

    @TaskAction
    fun start()
    {
        with(project.version as ProjectVersion){
            prodReady = true
        }

        // TODO: check how to enable ant.propertyfile()
        /*
        ant.propertyfile(file: destFile) {
            entry(key: "release", type: "string", operation: "=", value: "true")
        }
        */
    }
}
