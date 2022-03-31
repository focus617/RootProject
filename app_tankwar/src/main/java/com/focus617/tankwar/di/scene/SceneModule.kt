package com.focus617.tankwar.di.scene

import android.content.Context
import com.focus617.tankwar.scene.base.IfDraw
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object SceneModule {

    @TestScene
    @Provides
    fun bindTestScene(@ApplicationContext context: Context): IfDraw = MyTestScene(context)

    // TODO: check why qualifier can't work
//    @RealScene
//    @Provides
//    fun bindGameScene(@ApplicationContext context: Context): IDraw = GameScene(context)
}