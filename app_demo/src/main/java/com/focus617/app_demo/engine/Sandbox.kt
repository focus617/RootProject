package com.focus617.app_demo.engine

import android.content.Context
import com.focus617.app_demo.engine.d2.GameLayer
import com.focus617.app_demo.engine.d2.XGLScene2D
import com.focus617.app_demo.engine.d3.XGLScene3D
import com.focus617.app_demo.terrain.TerrainLayer
import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.core.LayerStack
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.scene.Scene
import java.io.Closeable

class Sandbox(context: Context, val is3D: Boolean) : Engine(), Closeable {

    var scene: Scene?

    init {
        if (is3D) {
            scene = XGLScene3D(context, this)
            pushLayer(TerrainLayer("TerrainLayer", scene as XGLScene3D, is3D))
            //pushOverLayer(Layer2D("ExampleOverlay"))
        } else {
            scene = XGLScene2D(context, this)
            pushLayer(GameLayer("GameLayer", scene as XGLScene2D, is3D))
        }


    }

    override fun close() {
        scene?.close()
    }

    fun getLayerStack(): LayerStack = mLayerStack

    override fun onUpdate(timeStep: TimeStep) {
        scene?.onUpdate(timeStep)
    }


}

