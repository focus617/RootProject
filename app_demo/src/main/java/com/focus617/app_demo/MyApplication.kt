package com.focus617.app_demo

import com.focus617.app_demo.engine.Sandbox
import com.focus617.platform.uicontroller.BaseApplication
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication : BaseApplication() {

    // EntryPoint for XGame
    lateinit var gameEngine: Sandbox

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        // 初始化引擎
        gameEngine = Sandbox(is3D = true)
    }

    override fun onTerminate() {
        gameEngine.onDestroy()
        super.onTerminate()
    }
}