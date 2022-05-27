package com.focus617.app_demo.engine.d3

import android.content.Context
import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.core.IfWindow
import com.focus617.core.engine.core.LayerStack
import com.focus617.core.engine.ecs.mine.system.PerspectiveCameraSystem
import com.focus617.core.platform.event.base.EventType
import com.focus617.opengles.terrain.TerrainLayer
import com.focus617.opengles.text.TextLayer2D
import com.focus617.opengles.text.TextLayer3D
import java.io.Closeable

class Sandbox3D(context: Context) : Engine(), Closeable {
    // Create root entity
    var xglResourceManager = XGL3DResourceManager(context, this)

    init {
        pushLayer(GamePlayerLayer("GamePlayerLayer"))
        pushLayer(TextLayer3D("TextLayer"))
        pushLayer(TerrainLayer("TerrainLayer", context))
        pushOverLayer(TextLayer2D("OverLayer"))

    }

    fun getLayerStack(): LayerStack = mLayerStack
    fun getOverLayerStack(): LayerStack = mOverlayStack

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


    // 处理各种触屏事件，例如可能引起相机位置变化的事件
    private fun registerEventHandlers() {
        eventDispatcher.register(EventType.TouchPress) { event ->
//            val hasConsumed = scene.mCameraController.onEvent(event)
            val hasConsumed = PerspectiveCameraSystem.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.PinchStart) { event ->
//            val hasConsumed = scene.mCameraController.onEvent(event)
            val hasConsumed = PerspectiveCameraSystem.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.PinchEnd) { event ->
//            val hasConsumed = scene.mCameraController.onEvent(event)
            val hasConsumed = PerspectiveCameraSystem.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.Pinch) { event ->
//            val hasConsumed = scene.mCameraController.onEvent(event)
            val hasConsumed = PerspectiveCameraSystem.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.SensorRotation) { event ->
//            val hasConsumed = scene.mCameraController.onEvent(event)
            val hasConsumed = PerspectiveCameraSystem.onEvent(event)
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

