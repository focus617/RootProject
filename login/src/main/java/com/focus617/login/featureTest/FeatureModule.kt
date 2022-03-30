package com.focus617.login.featureTest

import com.focus617.core.platform.event.AppLaunchedEvent
import com.focus617.core.platform.event.EventHandler
import com.focus617.login.featureTest.FeatureEventHandlers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object FeatureModule {
    @Provides
    @IntoSet
    fun appLaunchedHandler(
        handlers: FeatureEventHandlers
    ): EventHandler<AppLaunchedEvent> = handlers.appLaunchedHandler
}