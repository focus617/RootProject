package com.focus617.tankwar.scene.base

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.focus617.tankwar.scene.GameConfig

abstract class Node(name: String, context: Context) : Leaf(name) {

    protected var x: Int = 0
    protected var y: Int = 0

    private val paint = Paint()    // 画笔
    private val resource: Resources = context.resources
    private lateinit var bitmap: Bitmap

    protected fun initBitmap(bitmapId: Int) {
        bitmap = BitmapFactory.decodeResource(resource, bitmapId)
    }

    override fun draw(canvas: Canvas) {
        val srcRect = Rect(0, 0, bitmap.width, bitmap.height)
        val destRect = Rect(x, y, x + GameConfig.BLOCK_WIDTH, y + GameConfig.BLOCK_WIDTH)
        canvas.drawBitmap(bitmap, srcRect, destRect, paint)
    }

}