package com.focus617.tankwar.di.platform

import android.app.Application
import android.content.Context
import com.focus617.tankwar.ui.MyViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelFactoryModule {

    @Provides
    @Singleton
    fun provideViewModelFactory(@ApplicationContext context: Context): MyViewModelFactory =
        MyViewModelFactory(context as Application)

}