package com.focus617.core.engine.renderer

import com.focus617.core.ecs.mine.system.PerspectiveCameraSystem
import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.renderer.vertex.VertexArray
import com.focus617.core.platform.base.BaseEntity

abstract class XRenderer : BaseEntity() {

    fun getAPI(): RendererAPI.API = RendererAPI.getAPI()

    open fun beginScene() {
        // 在多线程渲染里，会把BeginScene函数放在RenderCommandQueue里执行
        // camera在多线程渲染的时候不能保证主线程是否正在更改Camera的相关信息
        synchronized(PerspectiveCameraSystem.Companion) {
            SceneData.sProjectionMatrix.setValue(PerspectiveCameraSystem.getProjectionMatrix())
            SceneData.sViewMatrix.setValue(PerspectiveCameraSystem.getCamera().getViewMatrix())
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

