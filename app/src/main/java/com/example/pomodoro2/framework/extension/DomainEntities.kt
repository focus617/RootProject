package com.example.pomodoro2.framework.extension

import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.features.infra.database.TaskEntity

/**
 * Map domain entity to DatabaseObject
 */
fun Task.asDatabaseEntity(): TaskEntity {
    return TaskEntity(
            id = this.id,
            title = this.title,
            imageId = this.imageId,
            priority = this.priority,
            createTime = this.createTime
        )
}