package com.example.pomodoro2.data

import com.example.pomodoro2.platform.data.ActivityDataSource
import com.example.pomodoro2.platform.data.InMemoryDataSource
import com.example.pomodoro2.platform.data.NetworkDataSource
import com.example.pomodoro2.platform.data.TaskDataSource

/**
 * Definition of container for each type of data source
 */
object DataSourceContainer {

    lateinit var roomTaskDataSource: TaskDataSource
    lateinit var roomActivityDataSource: ActivityDataSource
    lateinit var inMemoryDataSource: InMemoryDataSource
    lateinit var networkDataSource: NetworkDataSource


    fun inject(
        roomTaskDataSource: TaskDataSource,
        activityDataSource: ActivityDataSource,
        inMemoryDataSource: InMemoryDataSource,
        networkDataSource: NetworkDataSource
    ) {
        DataSourceContainer.roomTaskDataSource = roomTaskDataSource
        roomActivityDataSource = activityDataSource
        DataSourceContainer.inMemoryDataSource = inMemoryDataSource
        DataSourceContainer.networkDataSource = networkDataSource
    }
}