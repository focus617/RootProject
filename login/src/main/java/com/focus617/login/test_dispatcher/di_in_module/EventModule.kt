package com.focus617.login.test_dispatcher.di_in_module

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface EventModule {

//    @Multibinds
//    fun appLaunchedHandlers(): EventHandlers<AppLaunchedEvent>

//    @Multibinds
//    fun analyticsOptionHandlers(): EventHandlers<AnalyticsOptionEvent>

//    @Multibinds
//    fun crashlyticsOptionHandlers(): EventHandlers<CrashlyticsOptionEvent>
}
