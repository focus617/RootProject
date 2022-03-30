package com.focus617.tankwar.scene.components

import android.content.Context
import android.graphics.Canvas
import com.focus617.tankwar.R
import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.base.Node

class Tank(
    name: String,
    context: Context,
) : Node(name, context) {
    //在游戏棋盘上的坐标
    var xPos: Int = 0
    var yPos: Int = GameConfig.BLOCK_NUM_H - 1

    // 移动方向上的偏移
    var xDelta: Int = 0
    var yDelta: Int = 0

    init {
        initBitmap(R.drawable.ic_tank_good)
    }

    private val halfWidth = GameConfig.BLOCK_WIDTH * GameConfig.BLOCK_NUM_W / 2
    private val halfHeight = GameConfig.BLOCK_WIDTH * GameConfig.BLOCK_NUM_H / 2

    override fun draw(canvas: Canvas) {
        val xBias = canvas.width / 2 - halfWidth
        val yBias = canvas.height / 2 - halfHeight
        x = xBias + xPos * GameConfig.BLOCK_WIDTH + xDelta
        y = yBias + yPos * GameConfig.BLOCK_WIDTH - yDelta
        super.draw(canvas)

        yDelta += GameConfig.BLOCK_WIDTH/10
        if(yDelta > halfHeight*2 ){
            yDelta = 0
            xDelta += GameConfig.BLOCK_WIDTH
        }
    }

}