package com.example.pomodoro2.domain.model

import com.example.pomodoro2.platform.domain.BaseAggregateRoot
import java.io.Serializable
import java.util.UUID

/**
 * 表示项目（目标）的数据类，用来存储创建的项目，并提供给ProjectFragment
 */
data class Task (
    var id: String = UUID.randomUUID().toString(),
    var title: String,
    var description: String = "",
    var isCompleted: Boolean = false,
    var imageId: Int,
    var priority: Int,
    var createTime: Long = System.currentTimeMillis()
): BaseAggregateRoot(), Serializable {

    val titleForList: String
        get() = if (title.isNotEmpty()) title else description

    val isActive
        get() = !isCompleted

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()

    companion object {
        val DefaultTask = Task(
            title = "番茄工作法",
            imageId = 0,
            priority = 1
        )
    }
}

// TODO: build Task Factory in Domain/Repository