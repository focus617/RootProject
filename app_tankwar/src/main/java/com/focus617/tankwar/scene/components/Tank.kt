package com.focus617.tankwar.scene.components

import android.content.Context
import android.graphics.Canvas
import com.focus617.tankwar.R
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.GameConfig
import com.focus617.tankwar.scene.base.MovableNode

class Tank(
    name: String,
    context: Context,
    override var xPos: Int = 0,
    override var yPos: Int = 0,
    override var dir: Dir = Dir.RIGHT
) : MovableNode(name, context) {

    init {
        initBitmap(R.drawable.ic_tank_good_up)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
    }
}
