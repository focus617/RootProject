package com.example.pomodoro2.framework

import android.app.Application
import com.example.pomodoro2.data.DataSourceContainer
import com.example.pomodoro2.features.data.database.RoomActivityDataSource
import com.example.pomodoro2.features.data.database.RoomTaskDataSource
import com.example.pomodoro2.data.AppInMemoryDataSource
import com.example.pomodoro2.features.data.network.AppNetworkDataSource
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        Timber.plant(Timber.DebugTree())

        // build the singleton data source
        DataSourceContainer.inject(
            RoomTaskDataSource(this),
            RoomActivityDataSource(this),
            AppInMemoryDataSource(),
            AppNetworkDataSource(this)
        )
    }

    companion object {
        lateinit var INSTANCE: BaseApplication
    }
}