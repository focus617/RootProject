package com.focus617.core.engine.core

import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher

open class Engine : BaseEntity(), Runnable {

    // Game线程
    private var threadCore: Thread? = null
    private var isRunning: Boolean = true
    private var mLastFrameTime: Long = 0

    var mWindow: IfWindow? = null

    protected val mLayerStack: LayerStack = LayerStack()
    protected val mOverlayStack: LayerStack = LayerStack()

    protected val eventDispatcher = EventDispatcher()

    init {
        threadCore = Thread(this)
        threadCore!!.start()
    }

    open fun onAttachWindow(window: IfWindow) {
        LOG.info("Window Attached")
        mWindow = window
        mWindow!!.setEventCallbackFn {
            onEvent(it)
        }
    }

    open fun onDetachWindow() {
        LOG.info("Window Detached")
        mWindow = null
    }

    /**
     * 销毁时, 停止线程的运行，防止内存泄漏
     */
    fun onDestroy() {
        threadCore?.interrupt()
        threadCore = null

    }

    // 如果事件可以被本地消费，则返回true，否则false
    private fun onEvent(event: Event): Boolean {
        eventDispatcher.dispatch(event)
        if (event.hasBeenHandled) return true

        for (index in mOverlayStack.mLayers.size - 1 downTo 0) {
            mOverlayStack.mLayers[index].onEvent(event)
            if (event.hasBeenHandled) break
        }
        if (event.hasBeenHandled) return true

        for (index in mLayerStack.mLayers.size - 1 downTo 0) {
            mLayerStack.mLayers[index].onEvent(event)
            if (event.hasBeenHandled) break
        }
        return event.hasBeenHandled
    }

    override fun run() {
        while (isRunning) {
            // 全平台通用的封装的API, OpenGL上就是glfwGetTime();
            // 虽然不同机器执行一次Loop函数的用时不同，但只要把每一帧里的运动，
            // 跟该帧所经历的时间相乘，就能抵消因为帧率导致的数据不一致的问题。
            // 注意, 这里time - m_LastFrameTIme, 正好算的应该是当前帧所经历的时间,
            // 而不是上一帧经历的时间
            val time: Long = System.currentTimeMillis()
            val timeStep: TimeStep = TimeStep(time - mLastFrameTime)
            mLastFrameTime = time

            // Update global data, such as scene
            this.onUpdate(timeStep)

            // Update game objects in each layer
            for (layer in mLayerStack.mLayers) {
                layer.onUpdate(timeStep)
            }

            // 如果Window处于onDetach状态时，不再更新Overlay
            mWindow?.apply {
                for (layer in mOverlayStack.mLayers) {
                    layer.onUpdate(timeStep)
                }
            }
            // 通知Renderer绘制UI
            mWindow?.onUpdate()

            //通过线程休眠以控制刷新速度
            try {
                Thread.sleep(SLEEP_INTERVAL)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    // Used for updating the global resource, such as objects in scene
    open fun onUpdate(timeStep: TimeStep){ }

    fun pushLayer(layer: Layer) {
        mLayerStack.PushLayer(layer)
    }

    fun pushOverLayer(layer: Layer) {
        mOverlayStack.PushLayer(layer)
    }

    companion object {
        const val SLEEP_INTERVAL = 100L

    }
}