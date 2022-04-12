package com.focus617.tankwar.di.network

import com.focus617.mylib.netty.api.IfNorthBoundChannel
import com.focus617.mylib.netty.northbound.NorthBoundChannel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun networkDataSourceFlow(dataSource: NorthBoundChannel): IfNorthBoundChannel

}
