package com.focus617.tankwar.di.scene

import android.content.Context
import android.graphics.*
import com.focus617.tankwar.ui.game.IDraw

class MyTestScene(context: Context) : IDraw {

    private val resource = context.resources
    private var step: Float = 0F

    override fun draw(canvas: Canvas) {
        val paint = Paint()    // 画笔
        canvas.run {
            paint.color = Color.BLUE          //设置画笔颜色
            drawRect(0F, 0F, 100F, 100F, paint)

            save()
            paint.color = Color.RED           //设置画笔颜色
            paint.style = Paint.Style.FILL    //设置填充样式
            paint.strokeWidth = 5F            //设置画笔宽度
            paint.isAntiAlias = true
            rotate(step, (width / 2).toFloat(), (height / 2).toFloat())
            drawLine(
                (width / 2).toFloat(),
                (height / 2).toFloat(),
                (width / 2).toFloat(),
                height.toFloat(),
                paint
            )
            restore()

            step += 1F
        }
    }

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