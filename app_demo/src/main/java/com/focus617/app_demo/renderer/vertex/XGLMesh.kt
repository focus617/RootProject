package com.focus617.app_demo.renderer.vertex

import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.vertex.VertexArray
import com.focus617.core.engine.scene_graph.renderer.IfModelLoader
import com.focus617.core.engine.scene_graph.renderer.Mesh

class XGLMesh : Mesh() {
    // vertexArray is initialized via calling XGLVertexArray.buildVertexArray
    override lateinit var vertexArray: VertexArray

    fun initOpenGlResource(model: IfModelLoader) {
        vertexArray = XGLVertexArray.buildVertexArray(model)
    }

    override fun draw() {
        vertexArray.bind()

        RenderCommand.drawIndexed(vertexArray)

        // 下面这行可以省略，以节约GPU的运行资源；
        // 在下个submit，会bind其它VertexArray，自然会实现unbind
        vertexArray.unbind()
    }
}