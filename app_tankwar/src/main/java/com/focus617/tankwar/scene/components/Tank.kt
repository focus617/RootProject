package com.focus617.tankwar.scene.components

import android.content.Context
import com.focus617.tankwar.R
import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.MovableNode

class Tank(
    name: String,
    context: Context,
    override var xPos: Int = 0,
    override var yPos: Int = 0,
    override var dir: Dir = Dir.RIGHT
) : MovableNode(name, context) {

    override var speed: Int = GameConfig.TANK_SPEED

    init {
        initBitmap(R.drawable.ic_tank_good_up)
    }

    override fun checkDir() {
        if ((xPos < 1) && (Dir.LEFT == dir)) {
            dir = Dir.RIGHT
            xPos = 0
        } else if ((xPos > GameConfig.BLOCK_NUM_W - 2) && (Dir.RIGHT == dir)) {
            dir = Dir.LEFT
            xPos = GameConfig.BLOCK_NUM_W - 1
        }

        if ((yPos < 1) && (Dir.UP == dir)) {
            dir = Dir.DOWN
            yPos = 0
        } else if ((yPos > GameConfig.BLOCK_NUM_H - 2) && (Dir.DOWN == dir)) {
            dir = Dir.UP
            yPos = GameConfig.BLOCK_NUM_H - 1
        }

    }
}
