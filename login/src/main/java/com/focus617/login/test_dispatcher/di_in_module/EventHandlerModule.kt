package com.focus617.login.test_dispatcher.di_in_module

import com.focus617.core.platform.event.applicationEvents.AppLaunchedEvent
import com.focus617.core.platform.event.applicationEvents.AppUpdateEvent
import com.focus617.core.platform.event.screenTouchEvents.TouchMovedEvent
import com.focus617.login.test_dispatcher.EventHandler
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
        handlers: AppLaunchedEventHandler
    ): EventHandler<AppLaunchedEvent> = handlers.appLaunchedHandler

    @Provides
    @IntoSet
    fun appUpdateHandler(
        handlers: AppUpdateEventHandler
    ): EventHandler<AppUpdateEvent> = handlers.appUpdateEventHandler

    @Provides
    @IntoSet
    fun viewOnTouchEventHandler(
        handlers: ViewOnTouchEventHandlers
    ): EventHandler<TouchMovedEvent> = handlers.viewOnTouchEventHandler
}