package com.focus617.tankwar.scene.base

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.focus617.platform.helper.BitmapHelper.draw
import com.focus617.tankwar.scene.GameConfig

/*
 * Node类的主要职责是负责在SurfaceView上绘制Bitmap
 */
abstract class Node(name: String, context: Context, val scene: IfScene) : Leaf(name) {
    //在游戏棋盘上的坐标
    abstract var xPos: Int
    abstract var yPos: Int

    // 被绘制对象是否仍有效（未出边界，未被摧毁）？
    protected var isAlive: Boolean = true

    open fun die(){
        this.isAlive = false
    }

    private val resource: Resources = context.resources

    protected val mapWidth = GameConfig.BLOCK_WIDTH * GameConfig.BLOCK_NUM_W
    protected val mapHeight = GameConfig.BLOCK_WIDTH * GameConfig.BLOCK_NUM_H

    var x: Int = 0
    var y: Int = 0

    override fun draw(canvas: Canvas) {
        // 重新计算自己当前的位置坐标(x,y)
        calculateCurrentPosition(canvas)

        // 找到自己此时合适的Bitmap
        findBitmap()

        // 在绘制以前，坐标(x,y)将由具体实现类进行计算更新
        bitmap.draw(canvas,x, y, x + GameConfig.BLOCK_WIDTH, y + GameConfig.BLOCK_WIDTH )

    }

    override fun refreshData() {
        // 执行策略检查
        checkStrategy()
    }

    // 实现子类需要负责在每次draw前重新计算自己当前的位置坐标(x,y)
    open fun calculateCurrentPosition(canvas: Canvas){
        val xBias = (canvas.width - mapWidth) / 2
        val yBias = (canvas.height - mapHeight) / 2
        x = xBias + xPos * GameConfig.BLOCK_WIDTH
        y = yBias + yPos * GameConfig.BLOCK_WIDTH
    }

    protected lateinit var bitmap: Bitmap
    // 实现子类需要负责在Scene中的BitmapRepository找到自己当时合适的Bitmap
    // 本函数将在每次draw之前被调用，以便根据移动方向，变换相应的Bitmap
    abstract fun findBitmap()

    // 实现子类需要负责实现自己的各项策略，比如移动到边界，或碰到障碍后的处理操作
    abstract fun checkStrategy()

}