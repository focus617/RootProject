package com.example.pomodoro2.features.infra.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


// 表示项目（目标）的数据类，用来存储创建的项目，并提供给ProjectFragment
@Parcelize
@Entity(tableName = "project_table")
data class Project(
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
): Parcelable

