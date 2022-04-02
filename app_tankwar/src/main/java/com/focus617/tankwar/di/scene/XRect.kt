package com.focus617.tankwar.di.scene

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.focus617.tankwar.scene.base.Leaf

class XRect(name: String) : Leaf(name) {

    private val paint = Paint()    // 画笔
    private var x: Float = 0F
    private var y: Float = 0F

    override fun draw(canvas: Canvas) {
        with(paint) {
            color = Color.BLUE          //设置画笔颜色
        }

        canvas.run {
            save()
            translate(x, y)
            drawRect(0F, 0F, 100F, 100F, paint)
            restore()
        }

        x++
        y++
    }

    override fun refreshData() {}

}
