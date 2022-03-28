package com.focus617.tankwar.ui.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceHolder
import android.view.SurfaceView

class XRenderer(
    context: Context,
    var surfaceView: SurfaceView
) : SurfaceHolder.Callback, Runnable {

    init {
        surfaceView.holder.addCallback(this)
        surfaceView.run {
            isFocusable = true
            isFocusableInTouchMode = true
            keepScreenOn = true
        }
    }

    private var isDrawing: Boolean = false

    override fun surfaceCreated(holder: SurfaceHolder) {
        isDrawing = true
        Thread(this).start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isDrawing = false
    }

    override fun run() {
        while (isDrawing) {
            draw()
            //通过线程休眠以控制刷新速度
            try {
                Thread.sleep(50)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    lateinit var canvas: Canvas
    private fun draw() {
        try {
            canvas = surfaceView.holder.lockCanvas()

            // 初始化画布并设置画布背景
            canvas.drawColor(Color.BLACK)

            //在画布上画一些东西
            onDraw(canvas)

        } catch (e: Exception) {
        } finally {
            surfaceView.holder.unlockCanvasAndPost(canvas)
        }
    }

    private var step: Float = 0F
    private fun onDraw(canvas: Canvas) {
        // 画笔
        val paint = Paint()

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