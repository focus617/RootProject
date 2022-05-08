package com.focus617.app_demo.engine.d2

import android.content.Context
import com.focus617.app_demo.renderer.XGLTextureSlots
import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.core.IfWindow
import com.focus617.core.engine.core.LayerStack
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.renderer.XRenderer
import com.focus617.core.engine.scene.Scene
import com.focus617.core.platform.event.base.EventType
import com.focus617.core.platform.event.screenTouchEvents.TouchDragEvent
import java.io.Closeable

class Sandbox2D(context: Context) : Engine(), Closeable {

    var scene: Scene = XGLScene2D(context, this)

    init {
        pushLayer(MapLayer("MayLayer", scene as XGLScene2D))
        pushLayer(GameLayer("GameLayer", scene as XGLScene2D))

    }

    fun getLayerStack(): LayerStack = mLayerStack

    override fun close() {
        scene.close()
    }

    override fun onAttachWindow(window: IfWindow) {
        super.onAttachWindow(window)
        registerEventHandlers()
    }

    override fun onDetachWindow() {
        super.onDetachWindow()
        unRegisterEventHandlers()
    }

    override fun beforeUpdate() {
        // 在多线程渲染里，会把BeginScene函数放在RenderCommandQueue里执行
        // camera在多线程渲染的时候不能保证主线程是否正在更改Camera的相关信息
        scene?.mCamera?.let {
            synchronized(it) {
                System.arraycopy(it.getProjectionMatrix(), 0, XRenderer.sProjectionMatrix, 0, 16)
                System.arraycopy(it.getViewMatrix(), 0, XRenderer.sViewMatrix, 0, 16)
            }
        }

        Renderer2DData.resetVertexBuffer()
        XGLTextureSlots.resetTextureSlot()
    }

    override fun afterUpdate() {
//        LOG.info(
//            "Statistic: drawCalls=${Renderer2DData.stats.drawCalls}," +
//                    " quadCount=${Renderer2DData.stats.quadCount}"
//        )
    }

    override fun onUpdate(timeStep: TimeStep) {
        scene?.onUpdate(timeStep)
    }

    // 处理各种触屏事件，例如可能引起相机位置变化的事件
    private fun registerEventHandlers() {
        eventDispatcher.register(EventType.TouchDrag) { event ->
            val e: TouchDragEvent = event as TouchDragEvent
            LOG.info("Engine: ${e.name} from ${e.source} received")
//                LOG.info("It's type is ${e.eventType}")
//                LOG.info("It's was submit at ${DateHelper.timeStampAsStr(e.timestamp)}")
//                LOG.info("Current position is (${e.x}, ${e.y})\n")
            val hasConsumed = scene.mCameraController.onEvent(event) ?: false
            hasConsumed
        }

        eventDispatcher.register(EventType.TouchPress) { event ->
            val hasConsumed = scene.mCameraController.onEvent(event) ?: false
            hasConsumed
        }

        eventDispatcher.register(EventType.PinchStart) { event ->
            val hasConsumed = scene.mCameraController.onEvent(event) ?: false
            hasConsumed
        }

        eventDispatcher.register(EventType.PinchEnd) { event ->
            val hasConsumed = scene.mCameraController.onEvent(event) ?: false
            hasConsumed
        }

        eventDispatcher.register(EventType.Pinch) { event ->
            val hasConsumed = scene.mCameraController.onEvent(event) ?: false
            hasConsumed
        }
    }

    private fun unRegisterEventHandlers() {
        eventDispatcher.unRegister(EventType.TouchDrag)
        eventDispatcher.unRegister(EventType.TouchPress)
        eventDispatcher.unRegister(EventType.PinchStart)
        eventDispatcher.unRegister(EventType.PinchEnd)
        eventDispatcher.unRegister(EventType.Pinch)
    }

}

