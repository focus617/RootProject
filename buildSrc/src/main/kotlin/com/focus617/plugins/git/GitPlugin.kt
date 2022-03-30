package com.example.pomodoro2.plugins.git

import com.example.pomodoro2.plugins.userInteractor.GradleUserInteractor
import com.focus617.plugins.git.GitCredentialsProvider
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.task


class GitPlugin : Plugin<Project> {

    private fun openGitRepository(
        project: Project,
        userInteractor: GradleUserInteractor
    ): GitRepository {
        val repo = GitRepository.openExisting(
            project.projectDir,
            GitCredentialsProvider(project, userInteractor)
        )
        userInteractor.info("Connect to git local repository: ${repo.directory}")
        return repo
    }

    override fun apply(project: Project) {
        // separate capabilities from conventions
        project.plugins.apply(BasePlugin::class)

        val userInteractor = GradleUserInteractor(project)
        val repo = openGitRepository(project, userInteractor)

        project.task<GitVersionTask>("getVersionFromGitTag") {
        }

        project.task("currentBranch"){
            group = "git"
            description = "Retrieve current branch from Git."
            doLast{userInteractor.info("Current branch: " + repo.getCurrentBranch())}
        }

        project.task("listTags"){
            group = "git"
            description = "Retrieve exist tags from Git."
            doLast{
                userInteractor.info("existing tag:")
                repo.listTags().forEach{
                    userInteractor.info(it)
                }
            }
        }
    }


}