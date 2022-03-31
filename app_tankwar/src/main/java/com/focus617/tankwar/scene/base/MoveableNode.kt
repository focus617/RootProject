package com.focus617.tankwar.scene.base

import android.content.Context
import android.graphics.Canvas
import com.focus617.tankwar.scene.GameConfig

abstract class MoveableNode(name: String, context: Context) : Node(name, context) {
    //在游戏棋盘上的坐标
    abstract var xPos: Int
    abstract var yPos: Int

    //移动方向
    abstract var dir: Dir

    // 移动方向上的偏移
    private var xDelta: Int = 0
    private var yDelta: Int = 0

    private val mapWidth = GameConfig.BLOCK_WIDTH * GameConfig.BLOCK_NUM_W
    private val mapHeight = GameConfig.BLOCK_WIDTH * GameConfig.BLOCK_NUM_H

    override fun draw(canvas: Canvas) {
        val xBias = (canvas.width - mapWidth) / 2
        val yBias = (canvas.height - mapHeight) / 2
        x = xBias + xPos * GameConfig.BLOCK_WIDTH + xDelta
        y = yBias + yPos * GameConfig.BLOCK_WIDTH + yDelta

        super.draw(canvas)

        refreshDeltaXY()
        checkDir()
    }

    private fun checkDir() {
        if (xPos < 0) {
            dir = Dir.RIGHT
            xPos = 0
        } else if (xPos > GameConfig.BLOCK_NUM_W - 1) {
            dir = Dir.LEFT
            xPos = GameConfig.BLOCK_NUM_W - 1
        }

        if (yPos < 0) {
            dir = Dir.DOWN
            yPos = 0
        } else if (yPos > GameConfig.BLOCK_NUM_H - 1) {
            dir = Dir.UP
            yPos = GameConfig.BLOCK_NUM_H - 1
        }

    }

    private fun refreshDeltaXY() {
        when (dir) {
            Dir.UP -> {
                yDelta -= GameConfig.TANK_SPEED
            }
            Dir.DOWN -> {
                yDelta += GameConfig.TANK_SPEED
            }
            Dir.LEFT -> {
                xDelta -= GameConfig.TANK_SPEED
            }
            Dir.RIGHT -> {
                xDelta += GameConfig.TANK_SPEED
            }
        }

        if (xDelta >= GameConfig.BLOCK_WIDTH) {
            xPos++
            xDelta = 0
        } else if (xDelta <= -(GameConfig.BLOCK_WIDTH)) {
            xPos--
            xDelta = 0
        }

        if (yDelta >= GameConfig.BLOCK_WIDTH) {
            yPos++
            yDelta = 0
        } else if (yDelta <= -(GameConfig.BLOCK_WIDTH)) {
            yPos--
            yDelta = 0
        }

    }

}