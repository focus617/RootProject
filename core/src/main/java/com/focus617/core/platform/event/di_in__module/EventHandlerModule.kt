package com.focus617.core.platform.event.di_in__module

import com.focus617.core.platform.event.applicationEvent.AppLaunchedEvent
import com.focus617.core.platform.event.applicationEvent.AppUpdateEvent
import com.focus617.core.platform.event.base.EventHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

/**
 * 本di位于能够处理事件的模块或层，用于注入EventHandler
 */
@Module
@InstallIn(SingletonComponent::class)
object EventHandlerModule {

    @Provides
    @IntoSet
    fun appLaunchedHandler(
        handlers: AppLaunchedEventHandlers
    ): EventHandler<AppLaunchedEvent> = handlers.appLaunchedHandler

    @Provides
    @IntoSet
    fun appUpdateHandler(
        handlers: AppUpdateEventHandlers
    ): EventHandler<AppUpdateEvent> = handlers.appUpdateEventHandler

}