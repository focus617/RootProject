package com.example.pomodoro2.plugins.utils

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.extra
import java.io.ByteArrayOutputStream


/**
 * 将版本信息存储在外部文件中,并配置构建脚本来读取它
 */
open class LoadVersionTask : DefaultTask() {
    @Input
    var property: String = "versionFile"

    @Input
    private lateinit var versionFile: VersionFile
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
        versionFile = VersionFile(project.file(versionFileName).toPath())
        logger.quiet("$name: version file '$versionFileName' exist.")
        return true
    }

    fun readVersion(): ProjectVersion = versionFile.getVersion()

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
    @Input
    var destFile: VersionFile? = null

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
        if(destFile == null) {
            val property = "versionFile"
            logger.quiet("$name: retrieve version file name from property: '$property'")
            val versionFileName = project.extra[property].toString()

            if (versionFileName == "") {
                throw GradleException(
                    "Required version extension doesn't exist: ${project.extra[property]}"
                )
            }
            destFile = VersionFile(project.file(versionFileName).toPath())
        }

        var version = destFile!!.getVersion()
        version.prodReady = true
        destFile!!.updateVersion(version)
    }
}


open class GitVersionTask : DefaultTask() {
    init {
        group = "versioning"
        description = "Retrieve App version from Git tag."
    }

    /**
     * 获取 git tag，可以作为其版本名称
     * @return tag 的名称
     */
    private fun getGitTag(): String {
        val stdout = ByteArrayOutputStream()
        project.exec {
            commandLine("git", "describe", "--abbrev=0", "--tags")
            standardOutput = stdout
        }
        return stdout.toString()
    }

    /**
     * 获取 git tag 的数量，可以作为其版本号
     * @return tag 的数量
     */
    private fun getGitTagNumber(): Int {
        val stdout = ByteArrayOutputStream()
        project.exec {
            commandLine("git", "tag", "--list")
            standardOutput = stdout
        }
        return stdout.toString().split("\n").size
    }

    @TaskAction
    fun start()
    {
        logger.quiet("VersionName from Git tag: ${getGitTag()}")

        logger.quiet("VersionCode from Git tag: ${getGitTagNumber()}")
    }
}
