package com.focus617.core.engine.renderer

import com.focus617.core.engine.scene.Camera
import com.focus617.core.platform.base.BaseEntity

open class XRenderer: BaseEntity() {
    //val mCamera = OrthographicCamera(-1.0f, 1.0f, -1.0f, 1.0f)
    val mCamera = Camera()

    fun getAPI(): RendererAPI.API = RendererAPI.getAPI()

    open fun beginScene(camera: Camera) {
        SceneData.sViewMatrix = camera.getViewMatrix()
    }

    open fun endScene() {

    }

    open fun submit(
        shader: Shader,
        vertexArray: VertexArray,
        transform: FloatArray
    ) {

    }

    companion object SceneData{
        var sViewMatrix: FloatArray = FloatArray(16)
    }
}

