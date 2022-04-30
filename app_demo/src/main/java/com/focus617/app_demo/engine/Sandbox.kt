package com.focus617.app_demo.engine

import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.scene.Camera
import com.focus617.core.engine.scene.OrthographicCamera
import com.focus617.core.engine.scene.PerspectiveCamera
import com.focus617.core.engine.scene.Scene
import java.io.Closeable

class Sandbox(private val is3D: Boolean) : Engine(), Closeable {

    private val camera: Camera =  if (is3D) PerspectiveCamera() else OrthographicCamera()
    val scene: Scene = Scene(is3D, camera)

    init {
        pushLayer(GameLayer("GameLayer", this, is3D))
        //pushOverLayer(Layer2D("ExampleOverlay"))
    }

    override fun close() {
        scene.close()
    }


}

