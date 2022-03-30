package com.focus617.tankwar.scene.components

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.focus617.tankwar.scene.base.Leaf

class ClockPin(name: String) : Leaf(name) {

    private val paint = Paint()    // 画笔
    private var step: Float = 0F

    override fun draw(canvas: Canvas) {

        with(paint){
            color = Color.RED           //设置画笔颜色
            style = Paint.Style.FILL    //设置填充样式
            strokeWidth = 8F            //设置画笔宽度
            isAntiAlias = true
        }

        with(canvas) {
            save()
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