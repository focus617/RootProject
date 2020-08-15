package com.example.pomodoro2.framework

import android.app.Application
import com.example.pomodoro2.data.implementation.DataSourceContainer
import com.example.pomodoro2.features.data.database.RoomActivityDataSource
import com.example.pomodoro2.features.data.database.RoomTaskDataSource
import com.example.pomodoro2.features.data.memory.AppInMemoryDataSource
import com.example.pomodoro2.features.data.network.AppNetworkDataSource
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        // build the singleton data source
        DataSourceContainer.inject(
            RoomTaskDataSource(this),
            RoomActivityDataSource(this),
            AppInMemoryDataSource(),
            AppNetworkDataSource(this)
        )
    }
}