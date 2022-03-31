package com.focus617.tankwar.scene.components

import android.content.Context
import android.graphics.Canvas
import com.focus617.tankwar.R
import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.MoveableNode

class Tank(
    name: String,
    context: Context,
) : MoveableNode(name, context) {

    override var xPos: Int = 0
    override var yPos: Int = 0
    override lateinit var dir: Dir

    init {
        initBitmap(R.drawable.ic_tank_good)
        xPos = 0
        yPos = GameConfig.BLOCK_NUM_H - 1
        dir = Dir.UP
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
    }
}
