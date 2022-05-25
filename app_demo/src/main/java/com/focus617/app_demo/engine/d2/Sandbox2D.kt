package com.focus617.app_demo.engine.d2

import android.content.Context
import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.core.IfWindow
import com.focus617.core.engine.core.LayerStack
import com.focus617.core.engine.ecs.mine.system.OrthographicCameraSystem
import com.focus617.core.platform.event.base.EventType
import com.focus617.core.platform.event.screenTouchEvents.TouchDragEvent
import com.focus617.opengles.renderer.texture.XGLTextureSlots
import java.io.Closeable

class Sandbox2D(context: Context) : Engine(), Closeable {
    // Create resource manager with context
    var xglResourceManager = XGL2DResourceManager(context, this)

    init {
        pushLayer(MapLayer("MapLayer", xglResourceManager))
        pushLayer(GameLayer("GameLayer", xglResourceManager))

    }

    fun getLayerStack(): LayerStack = mLayerStack

    override fun close() {
        xglResourceManager.close()
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
            val hasConsumed = OrthographicCameraSystem.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.TouchPress) { event ->
            val hasConsumed = OrthographicCameraSystem.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.PinchStart) { event ->
            val hasConsumed = OrthographicCameraSystem.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.PinchEnd) { event ->
            val hasConsumed = OrthographicCameraSystem.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.Pinch) { event ->
            val hasConsumed = OrthographicCameraSystem.onEvent(event)
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

