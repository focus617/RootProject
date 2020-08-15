package com.example.pomodoro2.features.data.network

import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.features.data.database.TaskEntity
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 *
 * @see domain package for
 */

/**
 * VideoHolder holds a list of Tasks.
 *
 * This is to parse first level of our network result which looks like
 *
 * {
 *   "tasks": []
 * }
 */

@JsonClass(generateAdapter = true)
data class NetworkTaskContainer(
    val tasks: List<NetworkTask>,
    val networkContact: NetworkContact
)

/**
 * Network object definition.
 */
@JsonClass(generateAdapter = true)
data class NetworkTask(
    var id: Long,
    var title: String,
    var imageId: Int,
    var priority: Int,
    var createTime: Long)

@JsonClass(generateAdapter = true)
data class Phone(
    val home: String,
    val mobile: String
)

@JsonClass(generateAdapter = true)
data class NetworkContact(
    val name: String,
    val email: String,
    val phone: Phone
)

/**
 * convenience method: Convert Network results to domain objects
 */
fun NetworkTaskContainer.asDomainModel(): List<Task> {
    return tasks.map {
        Task(
            id = it.id,
            title = it.title,
            imageId = it.imageId,
            priority = it.priority,
            createTime = it.createTime)
    }
}


/**
 * convenience method: Convert Network results to database objects
 */
fun NetworkTaskContainer.asDatabaseModel(): List<TaskEntity> {
    return tasks.map {
        TaskEntity(
            id = it.id,
            title = it.title,
            imageId = it.imageId,
            priority = it.priority,
            createTime = it.createTime)
    }
}
