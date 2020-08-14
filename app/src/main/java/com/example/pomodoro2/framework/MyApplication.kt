package com.example.pomodoro2.framework

import android.app.Application
import com.example.pomodoro2.data.DataSourceContainer
import com.example.pomodoro2.features.infra.database.RoomActivityDataSource
import com.example.pomodoro2.features.infra.database.RoomTaskDataSource
import com.example.pomodoro2.features.infra.memory.AppInMemoryDataSource
import com.example.pomodoro2.features.infra.network.AppNetworkDataSource
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