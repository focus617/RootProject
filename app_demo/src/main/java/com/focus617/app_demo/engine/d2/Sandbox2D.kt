package com.focus617.app_demo.engine.d2

import android.content.Context
import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.core.IfWindow
import com.focus617.core.engine.core.LayerStack
import com.focus617.core.engine.scene_graph.scene.Scene
import com.focus617.core.platform.event.base.EventType
import com.focus617.core.platform.event.screenTouchEvents.TouchDragEvent
import com.focus617.opengles.renderer.texture.XGLTextureSlots
import java.io.Closeable

class Sandbox2D(context: Context) : Engine(), Closeable {
    // Create root entity
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
        scene?.mCameraController?.let {
            synchronized(it) {
                XGLRenderer2D.SceneData.sProjectionMatrix.setValue(it.getProjectionMatrix())
                XGLRenderer2D.SceneData.sViewMatrix.setValue(it.getCamera().getViewMatrix())
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


    // 处理各种触屏事件，例如可能引起相机位置变化的事件
    private fun registerEventHandlers() {
        eventDispatcher.register(EventType.TouchDrag) { event ->
            val e: TouchDragEvent = event as TouchDragEvent
            LOG.info("Engine: ${e.name} from ${e.source} received")
//                LOG.info("It's type is ${e.eventType}")
//                LOG.info("It's was submit at ${DateHelper.timeStampAsStr(e.timestamp)}")
//                LOG.info("Current position is (${e.x}, ${e.y})\n")
            val hasConsumed = scene.mCameraController.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.TouchPress) { event ->
            val hasConsumed = scene.mCameraController.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.PinchStart) { event ->
            val hasConsumed = scene.mCameraController.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.PinchEnd) { event ->
            val hasConsumed = scene.mCameraController.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.Pinch) { event ->
            val hasConsumed = scene.mCameraController.onEvent(event)
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

