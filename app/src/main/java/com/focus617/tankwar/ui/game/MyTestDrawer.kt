package com.focus617.tankwar.ui.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class MyTestDrawer : IDraw {
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
}