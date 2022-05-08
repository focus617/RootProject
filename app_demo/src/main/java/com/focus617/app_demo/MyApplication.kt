package com.focus617.app_demo

import com.focus617.app_demo.engine.d2.Sandbox2D
import com.focus617.app_demo.engine.d3.Sandbox3D
import com.focus617.core.engine.core.Engine
import com.focus617.platform.uicontroller.BaseApplication
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication : BaseApplication() {

    // EntryPoint for XGame
    lateinit var gameEngine: Engine

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
        // is3D是选择2D，或3D的总开关
        val is3D = true

        gameEngine = if (is3D) Sandbox3D(this) else Sandbox2D(this, is3D = false)
    }

    override fun onTerminate() {
        gameEngine.close()
        super.onTerminate()
    }
}
