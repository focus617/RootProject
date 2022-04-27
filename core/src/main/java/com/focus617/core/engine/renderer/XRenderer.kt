package com.focus617.core.engine.renderer

import com.focus617.core.engine.scene.Camera
import com.focus617.core.engine.scene.CameraController
import com.focus617.core.platform.base.BaseEntity

abstract class XRenderer : BaseEntity() {
    abstract val mCameraController: CameraController

    fun getAPI(): RendererAPI.API = RendererAPI.getAPI()

    open fun initRenderer() {
        RenderCommand.init()
    }

    open fun beginScene(camera: Camera) {
        // 在多线程渲染里，会把BeginScene函数放在RenderCommandQueue里执行
        // camera在多线程渲染的时候不能保证主线程是否正在更改Camera的相关信息
        synchronized(camera) {
            System.arraycopy(camera.getProjectionMatrix(), 0, SceneData.sProjectionMatrix, 0, 16)
            System.arraycopy(camera.getViewMatrix(), 0, SceneData.sViewMatrix, 0, 16)
        }
    }

    open fun endScene() {

    }

    open fun submit(
        shader: Shader,
        vertexArray: VertexArray,
        transform: FloatArray
    ) {

    }

    companion object SceneData {
        var sProjectionMatrix: FloatArray = FloatArray(16)
        var sViewMatrix: FloatArray = FloatArray(16)
    }
}

