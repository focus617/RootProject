package com.example.pomodoro2.data

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
        DataSourceContainer.roomActivityDataSource = activityDataSource
        DataSourceContainer.inMemoryDataSource = inMemoryDataSource
        DataSourceContainer.networkDataSource = networkDataSource
    }
}