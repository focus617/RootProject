package com.focus617.core.engine.core

import com.focus617.core.platform.base.BaseEntity

open class Engine(window: IfWindow) : BaseEntity(), Runnable {

    // Game线程
    private var threadCore: Thread? = null
    private var isRunning: Boolean = true
    private val mWindow: IfWindow = window

    init {
        threadCore = Thread(this)
        threadCore!!.start()
    }

    /**
     * 销毁时, 停止线程的运行，防止内存泄漏
     */
    fun onDestroy() {
        threadCore?.interrupt()
        threadCore = null
    }

    override fun run() {
        while (isRunning) {
            mWindow.onUpdate()
            //通过线程休眠以控制刷新速度
            try {
                Thread.sleep(SLEEP_INTERVAL)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
    
    companion object {
        const val SLEEP_INTERVAL = 1000L
    }
}