package com.focus617.tankwar.di.scene

import android.content.Context
import com.focus617.tankwar.scene.GameScene
import com.focus617.tankwar.scene.base.IfRefresh
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object SceneModule {
    // TODO: check why qualifier can't work
//    @TestScene
//    @Provides
//    fun bindTestScene(@ApplicationContext context: Context): IfRefresh = MyTestScene(context)

    @RealScene
    @Provides
    fun bindGameScene(@ApplicationContext context: Context): IfRefresh = GameScene(context)
}