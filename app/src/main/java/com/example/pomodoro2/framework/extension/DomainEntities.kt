package com.example.pomodoro2.framework.extension

import com.example.pomodoro2.domain.model.Activity
import com.example.pomodoro2.domain.model.Task
import com.example.pomodoro2.features.data.database.ActivityEntity
import com.example.pomodoro2.features.data.database.TaskEntity

/**
 * Map domain entity to DatabaseObject
 */
fun Task.asDatabaseEntity(): TaskEntity {
    return TaskEntity(
            id = this.id,
            title = this.title,
            description = this.description,
            isCompleted = this.isCompleted,
            imageId = this.imageId,
            priority = this.priority,
            createTime = this.createTime
        )
}

fun Activity.asDatabaseEntity(): ActivityEntity {
    return ActivityEntity(
            id = this.id,
            title = this.title,
            focusDuration = this.focusDuration,
            taskId = this.taskId,
            priority = this.priority,
            createTime = this.createTime,
            focusCounter =this.focusCounter,
            totalTime = this.totalTime
        )
}