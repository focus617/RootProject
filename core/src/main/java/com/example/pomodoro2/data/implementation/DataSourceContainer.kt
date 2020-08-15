package com.example.pomodoro2.data.implementation

import com.example.pomodoro2.data.interface_def.ActivityDataSource
import com.example.pomodoro2.data.interface_def.InMemoryDataSource
import com.example.pomodoro2.data.interface_def.NetworkDataSource
import com.example.pomodoro2.data.interface_def.TaskDataSource

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