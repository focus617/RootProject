package com.focus617.bookreader

import android.os.Build
import androidx.work.*
import com.focus617.bookreader.framework.interactors.Interactors
import com.focus617.bookreader.worker.RefreshDataWorker
import com.focus617.core.platform.event.AppLaunchedEvent
import com.focus617.core.platform.event.AppVariant
import com.focus617.core.platform.event.EventDispatcher
import com.focus617.mylib.coroutine.di.ApplicationScope
import com.focus617.mylib.netty.api.IfNorthBoundChannel
import com.focus617.mylib.netty.client.NettyClient
import com.focus617.platform.uicontroller.BaseApplication
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : BaseApplication() {

    @Inject
    @ApplicationScope
    lateinit var applicationScope: CoroutineScope

    @Inject
    lateinit var interactors: Interactors

    @Inject
    lateinit var uiChannel: IfNorthBoundChannel

    @Inject
    lateinit var eventDispatcher: EventDispatcher<AppLaunchedEvent>

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

        delayedInit()

        // 发布应用的启动事件
        publishAppLaunchEvent()

        // 创建Netty客户端，并连接服务器
        client = NettyClient.ClientBuilder
            .initChannel(uiChannel)
            .startup()
    }

    override fun onTerminate() {
        client.closeConnect()
        super.onTerminate()
    }

    private fun publishAppLaunchEvent() {
        applicationScope.launch {
            eventDispatcher.dispatch(AppLaunchedEvent(AppVariant.MOBILE))
        }
    }


    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}