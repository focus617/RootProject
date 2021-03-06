package com.example.pomodoro2.domain

import java.io.Serializable

/**
 * 表示项目（目标）的数据类，用来存储创建的项目，并提供给ProjectFragment
 */
data class Task(
    var id: Long = 0L,
    var title: String,
    var description: String = "",
    var isCompleted: Boolean = false,
    var imageId: Int,
    var priority: Int,
    var createTime: Long = System.currentTimeMillis()
): Serializable {
    companion object {
        val DefaultTask = Task(title = "番茄工作法", imageId = 0, priority = 1)
    }
}