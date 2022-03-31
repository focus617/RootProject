package com.focus617.tankwar.scene.base

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.focus617.tankwar.scene.GameConfig

/*
 * Node类的主要职责是负责在SurfaceView上绘制Bitmap
 */
abstract class Node(name: String, context: Context) : Leaf(name) {

    protected lateinit var bitmap: Bitmap

    // 由各实现具体类，负责找到自己在Scene中的Bitmap
    // 本函数将在每次draw之前被调用，以便根据移动方向，变换相应的Bitmap
    abstract fun findBitmap()

    private val resource: Resources = context.resources
    private val paint = Paint()           // 画笔

    var x: Int = 0
    var y: Int = 0

    override fun draw(canvas: Canvas) {
        findBitmap()

        val srcRect = Rect(0, 0, bitmap.width, bitmap.height)
        val destRect = Rect(x, y, x + GameConfig.BLOCK_WIDTH, y + GameConfig.BLOCK_WIDTH)
        canvas.drawBitmap(bitmap, srcRect, destRect, paint)
    }

}