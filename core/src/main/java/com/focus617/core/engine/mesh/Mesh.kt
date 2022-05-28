package com.focus617.core.engine.mesh

import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.vertex.VertexArray
import com.focus617.core.platform.base.BaseEntity

class Mesh(vertexArray: VertexArray) : BaseEntity() {
    // vertexArray is initialized via calling XGLVertexArray.buildVertexArray(IfMeshable)
    private val mVertexArray: VertexArray = vertexArray

    fun draw() {
        mVertexArray.bind()

        RenderCommand.drawIndexed(mVertexArray)

        // 下面这行可以省略，以节约GPU的运行资源；
        // 在下个submit，会bind其它VertexArray，自然会实现unbind
        mVertexArray.unbind()
    }
}