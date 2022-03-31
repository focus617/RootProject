package com.focus617.tankwar.scene.components

import android.content.Context
import com.focus617.tankwar.R
import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.MovableNode

class Bullet(
    name: String,
    context: Context,
    override var xPos: Int,
    override var yPos: Int,
    override var dir: Dir
) : MovableNode(name, context) {

    override var speed: Int = GameConfig.BULLET_SPEED

    init {
        initBitmap(R.drawable.ic_bullet_up)
    }

    override fun checkDir() {
    }

}