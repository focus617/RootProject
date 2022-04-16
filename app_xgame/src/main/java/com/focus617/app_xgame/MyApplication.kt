package com.focus617.app_xgame

import com.focus617.app_xgame.engine.Sandbox
import com.focus617.platform.uicontroller.BaseApplication
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication : BaseApplication() {

    // EntryPoint for XGame
    lateinit var game: Sandbox
    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        game = Sandbox()
    }

    override fun onTerminate() {
        game.onDestroy()
        super.onTerminate()
    }


}