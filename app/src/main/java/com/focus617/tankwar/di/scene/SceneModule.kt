package com.focus617.tankwar.di.scene

import com.focus617.tankwar.ui.game.IDraw
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SceneModule {
    @Binds
    abstract fun bindScene(scene: MyTestScene): IDraw
}