package com.example.pomodoro2.features.projects.domain

import android.os.Parcelable
import androidx.lifecycle.Transformations.map
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pomodoro2.features.infra.database.DatabaseProject
import kotlinx.android.parcel.Parcelize


// 表示项目（目标）的数据类，用来存储创建的项目，并提供给ProjectFragment
@Parcelize
data class Project(
    var id: Long = 0L,
    var title: String,
    var imageId: Int,
    var priority: Int,
    var createTime: Long = System.currentTimeMillis()
): Parcelable

/**
 * Map domain entity to DatabaseObject
 */
fun Project.asDatabaseEntity(): DatabaseProject {
    return DatabaseProject(
            id = this.id,
            title = this.title,
            imageId = this.imageId,
            priority = this.priority,
            createTime = this.createTime
        )
}