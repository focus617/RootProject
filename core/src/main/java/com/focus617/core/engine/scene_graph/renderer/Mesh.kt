package com.focus617.core.engine.scene_graph.renderer

import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.vertex.VertexArray

class Mesh(val vertexArray: VertexArray) {
    // vertexArray is initialized via calling XGLVertexArray.buildVertexArray

    fun draw(){
        vertexArray.bind()

        RenderCommand.drawIndexed(vertexArray)

        // 下面这行可以省略，以节约GPU的运行资源；
        // 在下个submit，会bind其它VertexArray，自然会实现unbind
        vertexArray.unbind()
    }
}