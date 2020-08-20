package com.example.pomodoro2.data

import com.example.pomodoro2.domain.model.Task
import com.example.pomodoro2.platform.data.ActivityDataSource
import com.example.pomodoro2.platform.data.InMemoryDataSource
import com.example.pomodoro2.platform.data.INetworkDataSource
import com.example.pomodoro2.platform.data.IDbLikeDataSource

/*
* TODO: Study Service Locator pattern
* under architecture-samples: mock.ServiceLocator
*/

/**
 * Definition of container for each type of data source
 */
object DataSourceContainer {
    lateinit var remoteTaskDataSource: IDbLikeDataSource<Task>
    lateinit var roomTaskDataSource: IDbLikeDataSource<Task>
    lateinit var roomActivityDataSource: ActivityDataSource
    lateinit var inMemoryDataSource: InMemoryDataSource
    lateinit var networkDataSource: INetworkDataSource


    fun inject(
        remoteTaskDataSource: IDbLikeDataSource<Task>,
        roomTaskDataSource: IDbLikeDataSource<Task>,
        activityDataSource: ActivityDataSource,
        inMemoryDataSource: InMemoryDataSource,
        networkDataSource: INetworkDataSource
    ) {
        DataSourceContainer.remoteTaskDataSource = remoteTaskDataSource
        DataSourceContainer.roomTaskDataSource = roomTaskDataSource
        DataSourceContainer.roomActivityDataSource = activityDataSource
        DataSourceContainer.inMemoryDataSource = inMemoryDataSource
        DataSourceContainer.networkDataSource = networkDataSource
    }
}