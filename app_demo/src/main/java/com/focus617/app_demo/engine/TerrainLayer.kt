package com.focus617.app_demo.engine

import com.focus617.app_demo.terrain.Heightmap
import com.focus617.app_demo.terrain.SkyBox
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Vector3
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher
import com.focus617.core.platform.event.base.EventType
import com.focus617.core.platform.event.screenTouchEvents.*

class TerrainLayer(name: String, private val scene: XGLScene, val is3D: Boolean) : Layer(name) {
    private val eventDispatcher = EventDispatcher()

    init {
        val skyBox = SkyBox()
        skyBox.onTransform3D(
            Vector3(0.0f, 0.0f, 0.0f),
            Vector3(100f, 100f, 100f)
        )
        skyBox.shaderName = XGLScene.SkyBoxShaderFilePath
        skyBox.textureName = XGLScene.SkyBoxTextureFilePath
        gameObjectList.add(skyBox)

        val heightmap = Heightmap(scene.context, XGLScene.HeightMapBitmapFilePath)
        // Expand the heightmap's dimensions, but don't expand the height as
        // much so that we don't get insanely tall mountains.
        heightmap.onTransform3D(
            Vector3(0.0f, -0.05f, 0.0f),
            Vector3(100f, 50f, 100f)
        )
        heightmap.shaderName = XGLScene.HeightMapShaderFilePath
        heightmap.textureName = ""
        gameObjectList.add(heightmap)
    }

    companion object {

    }

    override fun onAttach() {
        LOG.info("${this.mDebugName} onAttach()")
        registerEventHandlers()
    }

    override fun onDetach() {
        LOG.info("${this.mDebugName} onDetach")
        unRegisterEventHandlers()
    }

    override fun onUpdate(timeStep: TimeStep) {
        //LOG.info("${this.mDebugName} onUpdate")
    }

    override fun onEvent(event: Event): Boolean {
        LOG.info("${this.mDebugName} onEvent")
        return eventDispatcher.dispatch(event)
    }

    override fun close() {
        LOG.info("${this.mDebugName} closed")
        eventDispatcher.close()
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
