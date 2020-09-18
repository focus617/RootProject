package com.example.pomodoro2.plugins.userInteractor

interface UserInteractor {
    fun promptQuestion(prompt: String, default: String? = null): String
    fun promptPassword(prompt: String): CharArray
    fun info(msg: String)
    fun error(msg: String)
}