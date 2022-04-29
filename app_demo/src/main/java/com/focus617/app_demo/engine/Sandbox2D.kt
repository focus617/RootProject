package com.focus617.app_demo.engine

import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.scene.OrthographicCamera
import com.focus617.core.engine.scene.Scene
import java.io.Closeable

class Sandbox2D : Engine(), Closeable {
    val scene: Scene = Scene(OrthographicCamera())

    init {
        pushLayer(Layer2D("ExampleLayer", this))
        //pushOverLayer(Layer2D("ExampleOverlay"))
    }

    override fun close() {
        scene.close()
    }


}

