package com.example.pomodoro2.plugins.utils

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.extra
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Path
import java.util.*

data class ProjectVersion(
    var major: Int,
    var minor: Int,
    var prodReady: Boolean = false
) {

    @Override
    override fun toString() = "$major.$minor${if (prodReady) "" else "-SNAPSHOT"}"

}

data class VersionFile(private val fileName: Path) {

    private val file: File = fileName.toFile()
    private val props = Properties()

    init {
        if (!file.exists()) {
            throw GradleException(
                "Required version file doesn't exist:" + file.canonicalPath
            )
        }
        props.load(file.inputStream())
    }

    fun getVersionFile() = file

    fun getVersion(): ProjectVersion {
        return ProjectVersion(
            props.getProperty("major").toInt(),
            props.getProperty("minor").toInt(),
            props.getProperty("release")!!.toBoolean()
        )
    }

    fun updateVersion(version: ProjectVersion) {
        props.setProperty("major", version.major.toString())
        props.setProperty("minor", version.minor.toString())
        props.setProperty("release", version.prodReady.toString())

        props.store(FileOutputStream(file), "")
    }

}
