package com.focus617.core.engine.renderer

import com.focus617.core.engine.scene.Camera
import com.focus617.core.engine.scene.PerspectiveCamera
import com.focus617.core.platform.base.BaseEntity

open class XRenderer: BaseEntity() {
    //val mCamera = OrthographicCamera()
    val mCamera: Camera = PerspectiveCamera()

    fun getAPI(): RendererAPI.API = RendererAPI.getAPI()

    open fun init(){
        RenderCommand.init()
    }

    open fun beginScene(camera: Camera) {
        SceneData.sProjectionMatrix = camera.getProjectionMatrix()
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
        var sProjectionMatrix: FloatArray = FloatArray(16)
        var sViewMatrix: FloatArray = FloatArray(16)
    }
}

