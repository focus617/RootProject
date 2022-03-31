package com.focus617.tankwar.scene.base

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.focus617.tankwar.scene.GameConfig

/*
 * Node类的主要职责是负责在SurfaceView上绘制Bitmap
 */
abstract class Node(name: String, context: Context, val scene: IfScene) : Leaf(name) {
    //在游戏棋盘上的坐标
    abstract var xPos: Int
    abstract var yPos: Int

    protected lateinit var bitmap: Bitmap

    // 由各实现具体类，负责找到自己在Scene中的Bitmap
    // 本函数将在每次draw之前被调用，以便根据移动方向，变换相应的Bitmap
    abstract fun findBitmap()

    // 被绘制对象是否仍有效（未出边界，未被摧毁）？
    protected var isAlive: Boolean = true

    open fun die(){
        this.isAlive = false
    }



    private val resource: Resources = context.resources
    private val paint = Paint()           // 画笔

    protected val mapWidth = GameConfig.BLOCK_WIDTH * GameConfig.BLOCK_NUM_W
    protected val mapHeight = GameConfig.BLOCK_WIDTH * GameConfig.BLOCK_NUM_H

    var x: Int = 0
    var y: Int = 0

    override fun draw(canvas: Canvas) {
        // 检查和销毁无效对象
        if (!this.isAlive){
            scene.rootNode.remove(this)
            return
        }

        findBitmap()

        // 在绘制以前，坐标(x,y)将由具体实现类进行计算更新
        val srcRect = Rect(0, 0, bitmap.width, bitmap.height)
        val destRect = Rect(x, y, x + GameConfig.BLOCK_WIDTH, y + GameConfig.BLOCK_WIDTH)
        canvas.drawBitmap(bitmap, srcRect, destRect, paint)
    }

}