package com.example.pomodoro2.features.infra.network

import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.features.infra.database.DatabaseTask
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 *
 * @see domain package for
 */

/**
 * VideoHolder holds a list of Projects.
 *
 * This is to parse first level of our network result which looks like
 *
 * {
 *   "projects": []
 * }
 */

@JsonClass(generateAdapter = true)
data class NetworkProjectContainer(
    val projects: List<NetworkProject>,
    val networkContact: NetworkContact
)

/**
 * Network object definition.
 */
@JsonClass(generateAdapter = true)
data class NetworkProject(
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
fun NetworkProjectContainer.asDomainModel(): List<Task> {
    return projects.map {
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
fun NetworkProjectContainer.asDatabaseModel(): List<DatabaseTask> {
    return projects.map {
        DatabaseTask(
            id = it.id,
            title = it.title,
            imageId = it.imageId,
            priority = it.priority,
            createTime = it.createTime)
    }
}
