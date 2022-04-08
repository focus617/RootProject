package com.focus617.tankwar.ui.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.focus617.tankwar.scene.base.IfRefresh

const val SLEEP_INTERVAL = 40L

class XRenderer(
    var surfaceView: SurfaceView,
    var scene: IfRefresh
) : SurfaceHolder.Callback, Runnable {

    private var holder: SurfaceHolder? = null
    private lateinit var canvas: Canvas     // 用于绘图的canvas

    private var threadDrawing: Thread? = null      // 刷新绘图子线程
    private var threadRefreshData: Thread? = null  // 刷新数据子线程
    private var isDrawing: Boolean = false

    init {
        with(surfaceView) {
            isFocusable = true
            isFocusableInTouchMode = true
            keepScreenOn = true

            // 设置透明背景:
            // setZOrderOnTop(true) 必须在holder.setFormat方法之前，
            // 不然png的透明效果不生效
            setZOrderOnTop(false)
        }

        // 初始化一个 SurfaceHolder 对象并注册 SurfaceHolder 的回调方法
        holder = surfaceView.holder
        holder!!.addCallback(this)
        holder!!.setFormat(PixelFormat.TRANSLUCENT)

//        NettyClient.startup()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // 清除未停止的线程
        threadDrawing?.interrupt()
        threadRefreshData?.interrupt()

        threadDrawing = Thread(this)
        threadRefreshData = Thread {
            while (isDrawing) {
//                scene.refreshData()
                //通过线程休眠以控制刷新速度
                try {
                    Thread.sleep(SLEEP_INTERVAL)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        isDrawing = true
        threadDrawing!!.start()
//        threadRefreshData!!.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isDrawing = false
        onDestroy()
    }

    /**
     * 防止内存泄漏
     */
    private fun onDestroy() {
        // 当surfaceView销毁时, 停止线程的运行.
        // 避免surfaceView销毁之后，线程还在运行而报错.
        threadDrawing?.interrupt()
        threadDrawing = null
        threadRefreshData?.interrupt()
        threadRefreshData = null
    }

    override fun run() {
        while (isDrawing) {
            drawView()
            //通过线程休眠以控制刷新速度
            try {
                Thread.sleep(SLEEP_INTERVAL)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private fun drawView() {
        val surfaceHolder = surfaceView.holder

        // 锁定画布
        synchronized(surfaceHolder) {
            try {
                canvas = surfaceHolder.lockCanvas()

                // 初始化画布并设置画布背景
                canvas.drawColor(Color.BLACK)

                //在画布上画出场景
                scene.draw(canvas)

                //TODO: Can we separate two i/f into two threads? How to avoid concurrency problem?
                //刷新各个对象
                scene.refreshData()

            } catch (e: Exception) {
            } finally {
                surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
    }

}