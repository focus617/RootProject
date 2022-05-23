package com.focus617.core.engine.core

import com.focus617.core.ecs.fleks.Entity
import com.focus617.core.ecs.fleks.World
import com.focus617.core.ecs.mine.component.Camera
import com.focus617.core.ecs.mine.component.CameraController
import com.focus617.core.ecs.mine.component.PositionComponentListener
import com.focus617.core.ecs.mine.component.Relationship
import com.focus617.core.ecs.mine.system.DayNightSystem
import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher
import java.io.Closeable

open class Engine : BaseEntity(), Runnable, Closeable {

    val camera: Entity

    /** ++++++++ 构建ECS ++++++++++ */
    private val eventManager = DayNightSystem.EventManager()
    val ecsScene = World {
        entityCapacity = 600

        system<DayNightSystem>()
        inject(eventManager)

        // register the listener to the world
        componentListener<PositionComponentListener>()
    }
    /** +++++++++++++++++++++++++++ */

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

        camera = ecsScene.entity {
            add<Camera>()
            add<CameraController>()
            add<Relationship>()
        }
    }

    /**
     * 销毁时, 停止线程的运行，防止内存泄漏
     */
    /**
     * 销毁时, 停止线程的运行，防止内存泄漏
     */
    override fun close() {
        isRunning = false
        threadCore?.interrupt()
        threadCore = null
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
            /**
             * 全平台通用的封装的API
             * 虽然不同机器执行一次Loop函数的用时不同，但只要把每一帧里的运动，
             * 跟该帧所经历的时间相乘，就能抵消因为帧率导致的数据不一致的问题。
             * 注意, 这里time - m_LastFrameTIme, 正好算的应该是当前帧所经历的时间,
             * 而不是上一帧经历的时间
             */
            /**
             * 全平台通用的封装的API
             * 虽然不同机器执行一次Loop函数的用时不同，但只要把每一帧里的运动，
             * 跟该帧所经历的时间相乘，就能抵消因为帧率导致的数据不一致的问题。
             * 注意, 这里time - m_LastFrameTIme, 正好算的应该是当前帧所经历的时间,
             * 而不是上一帧经历的时间
             */
            val time: Long = System.currentTimeMillis()
            val timeStep: TimeStep = TimeStep(time - mLastFrameTime)
            mLastFrameTime = time

            // Update global data, such as scene
            this.onUpdate(timeStep)
            // Plan to replace above with ECS
            ecsScene.update(timeStep.getMilliSecond().toFloat())

            // Update game objects in each layer
            beforeUpdate()
            for (layer in mLayerStack.mLayers) {
                layer.onUpdate(timeStep)
            }
            afterUpdate()

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
    open fun onUpdate(timeStep: TimeStep) {}

    fun pushLayer(layer: Layer) {
        mLayerStack.PushLayer(layer)
    }

    fun pushOverLayer(layer: Layer) {
        mOverlayStack.PushLayer(layer)
    }

    open fun beforeUpdate() {}
    open fun afterUpdate() {}

    companion object {
        const val SLEEP_INTERVAL = 100L

    }

}