package com.focus617.app_demo.terrain

import com.focus617.app_demo.engine.d3.XGLScene3D
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Vector3
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher

class TerrainLayer(name: String, private val scene: XGLScene3D, val is3D: Boolean) : Layer(name) {
    private val eventDispatcher = EventDispatcher()

    init {
        val heightmap = Heightmap(scene.context, Heightmap.HeightMapBitmapFilePath)
        // Expand the heightmap's dimensions, but don't expand the height as
        // much so that we don't get insanely tall mountains.
        heightmap.onTransform3D(
            Vector3(0.0f, 0.0f, 0.0f),
            Vector3(100f, 10f, 100f)
        )
        gameObjectList.add(heightmap)

        val skyBox = SkyBox()
        skyBox.onTransform3D(
            Vector3(0.0f, 0.0f, 0.0f),
            Vector3(100f, 100f, 100f)
        )
        gameObjectList.add(skyBox)
    }

    override fun onAttach() {
        LOG.info("${this.mDebugName} onAttach()")
    }

    override fun onDetach() {
        LOG.info("${this.mDebugName} onDetach")
    }

    override fun onUpdate(timeStep: TimeStep) {
        //LOG.info("${this.mDebugName} onUpdate")
    }

    override fun onEvent(event: Event): Boolean {
        return eventDispatcher.dispatch(event)
    }

    override fun close() {
        LOG.info("${this.mDebugName} closed")
        eventDispatcher.close()
    }

}
