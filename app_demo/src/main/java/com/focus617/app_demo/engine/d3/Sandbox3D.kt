package com.focus617.app_demo.engine.d3

import android.content.Context
import com.focus617.app_demo.terrain.TerrainLayer
import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.core.IfWindow
import com.focus617.core.engine.core.LayerStack
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.scene.Scene
import com.focus617.core.platform.event.base.EventType
import com.focus617.core.platform.event.screenTouchEvents.TouchDragEvent
import java.io.Closeable

class Sandbox3D(context: Context) : Engine(), Closeable {

    var scene: Scene = XGLScene3D(context, this)

    init {
        pushLayer(GamePlayerLayer("GamePlayerLayer", scene as XGLScene3D))
        pushLayer(TerrainLayer("TerrainLayer", scene as XGLScene3D))
        //pushOverLayer(Layer2D("ExampleOverlay"))
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

        eventDispatcher.register(EventType.SensorRotation) { event ->
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
        eventDispatcher.unRegister(EventType.SensorRotation)
    }

}

