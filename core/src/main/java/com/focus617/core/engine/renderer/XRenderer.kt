package com.focus617.core.engine.renderer

import com.focus617.core.engine.scene.OrthographicCamera
import com.focus617.core.platform.base.BaseEntity

open class XRenderer: BaseEntity() {
    val mCamera = OrthographicCamera(-1.0f, 1.0f, -1.0f, 1.0f)

    fun getAPI(): RendererAPI.API = RendererAPI.getAPI()

    open fun beginScene(camera: OrthographicCamera) {
        SceneData.sViewProjectionMatrix = camera.getViewProjectionMatrix()
    }

    open fun endScene() {

    }

    open fun submit(shader: Shader, vertexArray: VertexArray) {
        shader.bind()
        vertexArray.bind()
        RenderCommand.drawIndexed(vertexArray)
    }

    companion object SceneData{
        var sViewProjectionMatrix: FloatArray = FloatArray(16)
    }
}

