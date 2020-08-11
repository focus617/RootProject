package com.example.pomodoro2.framework.extension

import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.features.infra.database.DatabaseTask

/**
 * Map domain entity to DatabaseObject
 */
fun Task.asDatabaseEntity(): DatabaseTask {
    return DatabaseTask(
            id = this.id,
            title = this.title,
            imageId = this.imageId,
            priority = this.priority,
            createTime = this.createTime
        )
}