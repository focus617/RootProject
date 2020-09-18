package com.example.pomodoro2.plugins.git

import com.example.pomodoro2.plugins.userInteractor.GradleUserInteractor
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.ByteArrayOutputStream


/**
 * Git Plugin tasks
 */
open class GitVersionTask : DefaultTask() {
    private val userInteractor = GradleUserInteractor(project)

    init {
        group = "git"
        description = "Retrieve git tags info from Git."
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
    fun start() {
        userInteractor.info("VersionName from Git tag: ${getGitTag()}")
        userInteractor.info("VersionCode from Git tag: ${getGitTagNumber()}")
    }
}
