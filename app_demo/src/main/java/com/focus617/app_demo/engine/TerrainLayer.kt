package com.focus617.app_demo.engine

import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.objects.d3.Cube
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher

class TerrainLayer(name: String, val engine: Engine, val is3D: Boolean) : Layer(name) {
    private val eventDispatcher = EventDispatcher()

    init {
        gameObjectList.add(Cube())

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
        LOG.info("${this.mDebugName} onEvent")
        return eventDispatcher.dispatch(event)
    }

    override fun close() {
        LOG.info("${this.mDebugName} closed")
        eventDispatcher.close()
    }



}
