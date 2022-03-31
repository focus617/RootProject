package com.focus617.tankwar.scene.base

import android.content.Context
import android.graphics.Canvas
import com.focus617.tankwar.scene.GameConfig

abstract class MovableNode(name: String, context: Context, val scene: RootNode) :
    Node(name, context) {
    //在游戏棋盘上的坐标
    abstract var xPos: Int
    abstract var yPos: Int

    //移动方向
    abstract var dir: Dir

    //移动速度
    abstract var speed: Int

    // 移动目标是否仍有效（未出边界，未爆炸）？
    protected var live: Boolean = true

    // 移动方向上的偏移
    private var xDelta: Int = 0
    private var yDelta: Int = 0

    private val mapWidth = GameConfig.BLOCK_WIDTH * GameConfig.BLOCK_NUM_W
    private val mapHeight = GameConfig.BLOCK_WIDTH * GameConfig.BLOCK_NUM_H

    // 子类需要负责实现移动到边界的处理操作
    open fun checkDir() {
        if (!this.live) scene.remove(this)
    }

    override fun draw(canvas: Canvas) {
        val xBias = (canvas.width - mapWidth) / 2
        val yBias = (canvas.height - mapHeight) / 2
        x = xBias + xPos * GameConfig.BLOCK_WIDTH + xDelta
        y = yBias + yPos * GameConfig.BLOCK_WIDTH + yDelta

        super.draw(canvas)

        move()
        checkDir()
    }

    private fun move() {
        when (dir) {
            Dir.UP -> {
                yDelta -= speed
            }
            Dir.DOWN -> {
                yDelta += speed
            }
            Dir.LEFT -> {
                xDelta -= speed
            }
            Dir.RIGHT -> {
                xDelta += speed
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