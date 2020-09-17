package com.example.pomodoro2.plugins.git

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.task


class GitPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        // separate capabilities from conventions
        project.plugins.apply(BasePlugin::class)


        project.task<GitVersionTask>("getVersionFromGitTag") {
        }
    }


}