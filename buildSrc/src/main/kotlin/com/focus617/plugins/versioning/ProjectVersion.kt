package com.example.pomodoro2.plugins.versioning

import org.gradle.api.GradleException
import org.gradle.api.logging.Logging
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Path
import java.util.*

class InvalidMajorMinorVersion(givenVersion: String) :
    Exception("Please specify valid version in x.y format, current is $givenVersion")

data class ProjectVersion(
    var major: Int,
    var minor: Int,
    var prodReady: Boolean = false
) {

    @Override
    override fun toString() = "$major.$minor${if (prodReady) "" else "-SNAPSHOT"}"

    fun assertValidMajorMinorVersion(version: String){
        val pattern = "(\\d+)\\.(\\d+)"
        if(!Regex(pattern).matches(version)){
            throw InvalidMajorMinorVersion(version)
        }
    }

    fun newMinorRelease(): ProjectVersion{
        this.minor += 1
        return this
    }
}


data class VersionFile(private val fileName: Path) {

    private val file: File = fileName.toFile()
    private val props = Properties()

    private val logger = Logging.getLogger(VersionFile::class.qualifiedName)

    init {
        checkPropertyFile(file)
    }

    fun getVersionFile() = file

    private fun checkPropertyFile(file: File): Boolean {
        logger.lifecycle("verifying the version property file: ${file.canonicalPath}")
        if ((!file.exists()) || (!file.isFile)) {
            throw GradleException(
                "Required version file doesn't exist:" + file.canonicalPath
            )
        }
        props.load(file.inputStream())
        if ((props.getProperty("major") == null) ||
            (props.getProperty("minor") == null) ||
            (props.getProperty("release") == null)
        ) {
            throw GradleException(
                "Required release property doesn't exist."
            )
        }
        return true
    }

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
