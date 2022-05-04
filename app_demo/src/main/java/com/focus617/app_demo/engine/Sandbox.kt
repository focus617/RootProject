package com.focus617.app_demo.engine

import android.content.Context
import com.focus617.app_demo.terrain.TerrainLayer
import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.core.LayerStack
import com.focus617.core.engine.core.TimeStep
import java.io.Closeable

class Sandbox(context: Context, val is3D: Boolean) : Engine(), Closeable {

    val scene: XGLScene = XGLScene(context, this, is3D)

    init {
        pushLayer(TerrainLayer("TerrainLayer", scene, is3D))
        //pushOverLayer(Layer2D("ExampleOverlay"))
    }

    override fun close() {
        scene.close()
    }

    fun getLayerStack(): LayerStack = mLayerStack

    override fun onUpdate(timeStep: TimeStep) {
        if(scene == null) return
        scene.onUpdate(timeStep)
    }


}

