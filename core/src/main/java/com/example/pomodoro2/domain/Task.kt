package com.example.pomodoro2.domain

import com.example.pomodoro2.platform.domain.BaseAggregate
import java.io.Serializable

/**
 * 表示项目（目标）的数据类，用来存储创建的项目，并提供给ProjectFragment
 */
data class Task (
    var id: Long = 0L,
    var title: String,
    var description: String = "",
    var isCompleted: Boolean = false,
    var imageId: Int,
    var priority: Int,
    var createTime: Long = System.currentTimeMillis()
): BaseAggregate(), Serializable {

    val titleForList: String
        get() = if (title.isNotEmpty()) title else description

    val isActive
        get() = !isCompleted

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()

    companion object {
        val DefaultTask = Task(title = "番茄工作法", imageId = 0, priority = 1)
    }
}