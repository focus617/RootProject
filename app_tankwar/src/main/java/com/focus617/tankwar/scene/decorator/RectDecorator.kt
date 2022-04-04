package com.focus617.tankwar.scene.decorator

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.focus617.tankwar.scene.base.Node

class RectDecorator(node: Node) : NodeDecorator(node) {

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        drawMask(canvas)
    }

    private fun drawMask(canvas: Canvas) {
        val paint = Paint()

        with(paint) {
            color = Color.RED           //设置画笔颜色
            style = Paint.Style.STROKE  //设置填充样式
            strokeWidth = 1F            //设置画笔宽度
            isAntiAlias = true
        }

        with(node) {
            canvas.drawLine(
                rect.left.toFloat(), rect.top.toFloat(),
                rect.right.toFloat(), rect.top.toFloat(), paint
            )
            canvas.drawLine(
                rect.left.toFloat(), rect.bottom.toFloat(),
                rect.right.toFloat(), rect.bottom.toFloat(), paint
            )
        }
    }
}