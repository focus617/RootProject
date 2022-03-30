package com.focus617.tankwar.di.scene

import android.content.Context
import android.graphics.*
import com.focus617.tankwar.scene.base.IDraw
import com.focus617.tankwar.scene.base.RootNode
import com.focus617.tankwar.scene.components.ClockPin
import com.focus617.tankwar.scene.components.XRect

class MyTestScene(context: Context) : IDraw {

    private val resource = context.resources
    private val rootNode = RootNode("Scene")

    init {
        with(rootNode) {
            add(ClockPin("clock"))
            add(XRect("BlueRect"))
        }

    }

    override fun draw(canvas: Canvas) = rootNode.draw(canvas)

    private fun drawBitmap(
        canvas: Canvas,
        bitmap: Bitmap,
        left: Float,
        top: Float
    ) {
        val paint = Paint()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        canvas.drawPaint(paint);
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC);
        paint.isAntiAlias = true;
        paint.style = Paint.Style.STROKE;

        canvas.drawBitmap(bitmap, left, top, paint)
    }
}