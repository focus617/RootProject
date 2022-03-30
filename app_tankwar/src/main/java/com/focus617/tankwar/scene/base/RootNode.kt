package com.focus617.tankwar.scene.base

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class RootNode(name: String) : Composite(name) {

    private val paint = Paint()    // 画笔

    override fun drawComposite(canvas: Canvas) {

        with(paint) {
            color = Color.YELLOW                 //设置画笔颜色
            style = Paint.Style.FILL             //设置填充样式
            strokeWidth = 20F                    //设置画笔宽度
            isAntiAlias = true
        }

        canvas.run {
            // 初始化画布并设置画布背景
            drawColor(Color.GREEN)

            drawLine(0F, 0F, 0F, height.toFloat(), paint)
            drawLine(0F, height.toFloat(), width.toFloat(), height.toFloat(), paint)
            drawLine(width.toFloat(), height.toFloat(), width.toFloat(), 0F, paint)
            drawLine(width.toFloat(), 0F, 0F, 0F, paint)
        }
    }
}