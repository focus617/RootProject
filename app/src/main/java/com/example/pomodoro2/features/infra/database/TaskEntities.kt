package com.example.pomodoro2.features.infra.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pomodoro2.domain.Task

/**
 * DatabaseTask represents a task entity in the database.
 * 表示任务（目标）的数据类，用来存储创建的任务项目，并提供给TaskFragment
 *
 * @param id          id of the task
 * @param title       title of the task
 * @param description description of the task
 * @param isCompleted whether or not this task is completed
 * @param imageId     id of the task's icon
 * @param priority    displaying order the task
 * @param createTime  timestampe of task creation
 */
@Entity(tableName = "TASK_TABLE")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    /* 如果希望与成员变量的名称不同，请通过name指定列的名称。*/
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "completed")
    var isCompleted: Boolean = false,

    @ColumnInfo(name = "imageID")
    var imageId: Int,

    /* 显示的顺序 */
    @ColumnInfo(name = "priority")
    var priority: Int,

    @ColumnInfo(name = "create_time")
    var createTime: Long = System.currentTimeMillis()
){
    val titleForList: String
        get() = if (title.isNotEmpty()) title else description


    val isActive
        get() = !isCompleted

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()
}

/**
 * Map DatabaseObject to domain entity
 */
fun TaskEntity.asDomainModel(): Task {
    return Task(
        id = this.id,
        title = this.title,
        description = this.description,
        isCompleted = this.isCompleted,
        imageId = this.imageId,
        priority = this.priority,
        createTime = this.createTime
    )
}

/**
 * Map DatabaseObjects to domain entities
 */
fun List<TaskEntity>.asDomainModel(): List<Task> {
    return map {
        Task(
            id = it.id,
            title = it.title,
            description = it.description,
            isCompleted = it.isCompleted,
            imageId = it.imageId,
            priority = it.priority,
            createTime = it.createTime
        )
    }
}