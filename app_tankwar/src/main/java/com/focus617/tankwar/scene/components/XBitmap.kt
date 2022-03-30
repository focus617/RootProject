package com.focus617.tankwar.scene.components

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import com.focus617.tankwar.scene.base.Node

class XBitmap(
    name: String,
    context: Context,
    private val bitmapId: Int
) : Node(name, context) {

    init {
        initBitmap()
        x = 0
        y = 1600
    }

    private fun initBitmap() {
        bitmap = BitmapFactory.decodeResource(resource, bitmapId)
    }

    override fun draw(canvas: Canvas) {
        with(paint) {
//            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
//            style = Paint.Style.FILL    //设置填充样式
//            strokeWidth = 8F            //设置画笔宽度
//            isAntiAlias = true
        }

        with(canvas) {

            val srcRect = Rect(0, 0, bitmap.width, bitmap.height)
            val destRect = Rect(x, y, x + TANK_UNIT, y + TANK_UNIT)
            drawBitmap(bitmap, srcRect, destRect, paint)
        }

        y -= TANK_UNIT / 10
        if (y <= 0) {
            y = canvas.height
            x += TANK_UNIT
        }
    }

    companion object {
        private const val TANK_UNIT = 150
    }
}