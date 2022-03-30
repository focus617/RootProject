package com.focus617.tankwar.scene.components

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.focus617.tankwar.scene.base.Node

class XRect(name: String) : Node(name) {

    private val paint = Paint()    // 画笔

    override fun draw(canvas: Canvas) {
        paint.color = Color.BLUE          //设置画笔颜色
        canvas.drawRect(0F, 0F, 100F, 100F, paint)
    }
}