package com.focus617.tankwar.di.scene

import android.content.Context
import com.focus617.tankwar.scene.GameScene
import com.focus617.tankwar.scene.base.IfRendererable
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
    fun bindGameScene(@ApplicationContext context: Context): IfRendererable =
        GameScene.TerrainBuilder(context)
            .loadGameConfig()
            .loadGameResource()     // 加载游戏资源
            .buildNodes()           // 初始化场景中的对象
            .build()
}