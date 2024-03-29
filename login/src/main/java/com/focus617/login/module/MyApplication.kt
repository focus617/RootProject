package com.focus617.login.module

import com.focus617.platform.uicontroller.BaseApplication
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

//@HiltAndroidApp
class MyApplication : BaseApplication() {
    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}