package com.example.pomodoro2.features.infra.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pomodoro2.domain.Task

/**
 * DatabaseTask represents a task entity in the database.
 * 表示任务（目标）的数据类，用来存储创建的任务项目，并提供给TaskFragment
 */
@Entity(tableName = "TASK_TABLE")
data class DatabaseTask(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    /* 如果希望与成员变量的名称不同，请通过name指定列的名称。*/
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "imageID")
    var imageId: Int,

    /* 显示的顺序 */
    @ColumnInfo(name = "priority")
    var priority: Int,

    @ColumnInfo(name = "create_time")
    var createTime: Long = System.currentTimeMillis()
)

/**
 * Map DatabaseVideos to domain entities
 */
fun List<DatabaseTask>.asDomainModel(): List<Task> {
    return map {
        Task(
            id = it.id,
            title = it.title,
            imageId = it.imageId,
            priority = it.priority,
            createTime = it.createTime
        )
    }
}