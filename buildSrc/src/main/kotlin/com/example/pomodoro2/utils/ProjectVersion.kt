package com.example.pomodoro2.utils

data class ProjectVersion(
    var major: Int,
    var minor: Int,
    var release: Boolean = false
    ) {

    @Override
    override fun toString() = "$major.$minor${if(release)"" else "-SNAPSHOT"}"
}