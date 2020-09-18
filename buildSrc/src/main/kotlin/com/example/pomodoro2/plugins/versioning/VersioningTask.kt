package com.example.pomodoro2.plugins.versioning

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.extra
import java.io.ByteArrayOutputStream

import com.example.pomodoro2.plugins.userInteractor.GradleUserInteractor


/**
 * 将版本信息存储在外部文件中,并配置构建脚本来读取它
 */
open class LoadVersionTask : DefaultTask() {
    @Input
    var property: String = "versionFile"

    @Input
    private lateinit var versionFile: VersionFile
    fun getVersionFile() = versionFile

    private val userInteractor = GradleUserInteractor(project)

    init {
        group = "versioning"
        description = "Retrieve the project version for property file."

        userInteractor.info("\n>> configure task: $name")
        checkVersionFile()
    }

    private fun checkVersionFile(): Boolean {

        userInteractor.info("$name: retrieve version file name from property:  '$property'")
        val versionFileName = project.extra[property].toString()

        if (versionFileName.isEmpty()) {
            val exc = GradleException(
                "Required version extension doesn't exist: ${project.extra[property]}"
            )
            userInteractor.error(exc.message?:"")
            throw exc
        }
        try {
            versionFile = VersionFile(project.file(versionFileName).toPath())
        } catch (exc: Exception){
            userInteractor.error(exc.message ?: "")
            throw exc
        }
        return true
    }

    fun readVersion(): ProjectVersion = versionFile.getVersion()

    @TaskAction
    fun loadVersion() {
        userInteractor.info("\n$name: loadVersion()")
        if (checkVersionFile())
            project.version = readVersion()
        userInteractor.info("Retrieved version is '${project.version}'")
    }
}


open class ReleaseVersionTask : DefaultTask() {
    @Input
    var release: Boolean = false

    @Input
    var destFile: VersionFile? = null

    private val userInteractor = GradleUserInteractor(project)

    init {
        group = "versioning"
        description = "Makes project a release version."
    }

    @TaskAction
    fun start() {
        with(project.version as ProjectVersion) {
            prodReady = true
        }

        // TODO: check how to enable ant.propertyfile()
        /*
        ant.propertyfile(file: destFile) {
            entry(key: "release", type: "string", operation: "=", value: "true")
        }
        */
        if (destFile == null) {
            val property = "versionFile"
            userInteractor.info("$name: retrieve version file name from property: '$property'")
            val versionFileName = project.extra[property].toString()

            if (versionFileName.isEmpty()) {
                val exc = GradleException(
                    "Required version extension doesn't exist: ${project.extra[property]}"
                )
                userInteractor.error(exc.message?:"")
                throw exc
            }
            destFile = VersionFile(project.file(versionFileName).toPath())
        }

        var version = destFile!!.getVersion()
        version.prodReady = true
        destFile!!.updateVersion(version)
    }
}

