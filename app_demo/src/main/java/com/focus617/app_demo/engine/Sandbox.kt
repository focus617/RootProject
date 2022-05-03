package com.focus617.app_demo.engine

import android.content.Context
import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.core.LayerStack
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.platform.event.base.EventType
import com.focus617.core.platform.event.screenTouchEvents.*
import java.io.Closeable

class Sandbox(context: Context, val is3D: Boolean) : Engine(), Closeable {

    val scene: XGLScene = XGLScene(context, this, is3D)

    init {
        registerEventHandlers()

        pushLayer(TerrainLayer("TerrainLayer", this, is3D))
        //pushOverLayer(Layer2D("ExampleOverlay"))
    }

    override fun close() {
        unRegisterEventHandlers()
        scene.close()
    }

    fun getLayerStack(): LayerStack = mLayerStack

    override fun onUpdate(timeStep: TimeStep) {
        if(scene == null) return
        scene.onUpdate(timeStep)
    }

    // 处理各种触屏事件，例如可能引起相机位置变化的事件
    private fun registerEventHandlers() {
        eventDispatcher.register(EventType.TouchDrag) { event ->
            val e: TouchDragEvent = event as TouchDragEvent
//                LOG.info("${this.mDebugName}: ${e.name} from ${e.source} received")
//                LOG.info("It's type is ${e.eventType}")
//                LOG.info("It's was submit at ${DateHelper.timeStampAsStr(e.timestamp)}")
//                LOG.info("Current position is (${e.x}, ${e.y})\n")
            val hasConsumed = scene.mCameraController.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.TouchPress) { event ->
            val e: TouchPressEvent = event as TouchPressEvent
            val hasConsumed = scene.mCameraController.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.PinchStart) { event ->
            val e: PinchStartEvent = event as PinchStartEvent
            val hasConsumed = scene.mCameraController.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.PinchEnd) { event ->
            val e: PinchEndEvent = event as PinchEndEvent
            val hasConsumed = scene.mCameraController.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.Pinch) { event ->
            val e: PinchEvent = event as PinchEvent
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

