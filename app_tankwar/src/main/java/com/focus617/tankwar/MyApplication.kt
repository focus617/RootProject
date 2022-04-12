package com.focus617.tankwar

import com.focus617.mylib.netty.api.IfNorthBoundChannel
import com.focus617.mylib.netty.client.NettyClient
import com.focus617.platform.uicontroller.BaseApplication
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : BaseApplication() {

    @Inject
    lateinit var uiChannel: IfNorthBoundChannel

    lateinit var client: NettyClient
    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        // 创建Netty客户端，并连接服务器
        client = NettyClient.ClientBuilder
            .initChannel(uiChannel)
            .startup()
    }

    override fun onTerminate() {
        client.closeConnect()
        super.onTerminate()
    }


}