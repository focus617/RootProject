package com.focus617.core.engine.renderer

import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.renderer.vertex.VertexArray
import com.focus617.core.engine.scene_graph.components.camera.CameraController
import com.focus617.core.platform.base.BaseEntity

abstract class XRenderer : BaseEntity() {

    fun getAPI(): RendererAPI.API = RendererAPI.getAPI()

    open fun beginScene(cameraController: CameraController) {
        // 在多线程渲染里，会把BeginScene函数放在RenderCommandQueue里执行
        // camera在多线程渲染的时候不能保证主线程是否正在更改Camera的相关信息
        synchronized(cameraController) {
//            System.arraycopy(cameraController.getProjectionMatrix(), 0, SceneData.sProjectionMatrix, 0, 16)
//            System.arraycopy(cameraController.mCamera.getViewMatrix(), 0, SceneData.sViewMatrix, 0, 16)
            SceneData.sProjectionMatrix.setValue(cameraController.getProjectionMatrix())
            SceneData.sViewMatrix.setValue(cameraController.getCamera().getViewMatrix())
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
        var sProjectionMatrix: Mat4 = Mat4()
        var sViewMatrix: Mat4 = Mat4()
    }
}

