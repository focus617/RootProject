package com.focus617.tankwar.ui.game

import android.graphics.Canvas
import android.graphics.Color
import android.view.SurfaceHolder
import android.view.SurfaceView

const val SLEEP_INTERVAL = 50L

class XRenderer(
    var surfaceView: SurfaceView,
    var drawer: IDraw
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
                Thread.sleep(SLEEP_INTERVAL)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private lateinit var canvas: Canvas
    private fun draw() {
        try {
            canvas = surfaceView.holder.lockCanvas()

            // 初始化画布并设置画布背景
            canvas.drawColor(Color.BLACK)

            //在画布上画一些东西
            drawer.draw(canvas)

        } catch (e: Exception) {
        } finally {
            surfaceView.holder.unlockCanvasAndPost(canvas)
        }
    }
}