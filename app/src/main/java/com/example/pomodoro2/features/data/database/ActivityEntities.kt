package com.example.pomodoro2.features.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pomodoro2.domain.model.Activity

/**
 * DatabaseTask represents a task entity in the database.
 * 表示活动的数据类，用来存储任务执行的各项活动，并提供给ActivitiesFragment
 */
@Entity(tableName = "ACTIVITY_TABLE")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    /* 如果希望与成员变量的名称不同，请通过name指定列的名称。*/
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "focusDuration")
    var focusDuration: Long,

    @ColumnInfo(name = "taskID")
    var taskId: String,

    /* 显示的顺序 */
    @ColumnInfo(name = "priority")
    var priority: Int,

    @ColumnInfo(name = "createTimeStamp")
    var createTime: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "focusCounter")
    var focusCounter: Long = 0L,

    @ColumnInfo(name = "total_time")
    var totalTime: Long = 0L
)

/**
 * Map DatabaseVideos to domain entities
 */
fun List<ActivityEntity>.asDomainModel(): List<Activity> {
    return map {
        Activity(
            id = it.id,
            title = it.title,
            focusDuration = it.focusDuration,
            taskId = it.taskId,
            priority = it.priority,
            createTime = it.createTime,
            focusCounter = it.focusCounter,
            totalTime = it.totalTime
        )
    }
}