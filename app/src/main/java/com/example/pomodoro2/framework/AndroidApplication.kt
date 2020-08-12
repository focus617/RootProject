package com.example.pomodoro2.framework

import android.app.Application
import timber.log.Timber

class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}