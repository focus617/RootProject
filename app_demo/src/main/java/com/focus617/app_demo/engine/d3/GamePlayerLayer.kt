package com.focus617.app_demo.engine.d3

import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Vector3
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher

class GamePlayerLayer(name: String, private val scene: XGLScene3D, val is3D: Boolean) : Layer(name) {
    private val eventDispatcher = EventDispatcher()

    private val earth = Earth()

    init{
        val box = Box()
        box.onTransform3D(
            Vector3(1.5f, 0.5f, 1.5f),
            Vector3(1.0f, 1.0f, 1.0f)
        )
        gameObjectList.add(box)

        earth.onTransform3D(
            Vector3(-1.5f, 1.0f, -1.5f),
            Vector3(1.0f, 1.0f, 1.0f)
        )
        gameObjectList.add(earth)
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
        earth.updateCameraPosition(scene.mCamera.getPosition())
    }

    override fun onEvent(event: Event): Boolean {
        return eventDispatcher.dispatch(event)
    }

    override fun close() {
        LOG.info("${this.mDebugName} closed")
        eventDispatcher.close()
    }

    // 处理各种触屏事件，例如可能引起相机位置变化的事件
    private fun registerEventHandlers() {}
    private fun unRegisterEventHandlers() {}

}