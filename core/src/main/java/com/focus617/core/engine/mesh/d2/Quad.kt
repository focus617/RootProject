package com.focus617.core.engine.mesh.d2

import com.focus617.core.engine.mesh.IfMeshable
import com.focus617.core.engine.renderer.vertex.BufferElement
import com.focus617.core.engine.renderer.vertex.BufferLayout
import com.focus617.core.engine.renderer.vertex.ShaderDataType

class Quad : IfMeshable {

    override fun getVertices(): FloatArray = floatArrayOf(
        // 每个顶点有2个顶点属性一位置、颜色、纹理（TODO： TextureId）
        // x,   y,    z, color.r,   .b,  .g,   .a,  TextureX, TextureY
        -0.5f, -0.5f, 0.0f, 0.8f, 0.3f, 0.2f, 1.0f, 0.0f, 0.0f,
        0.5f, -0.5f, 0.0f, 0.2f, 0.3f, 0.8f, 1.0f, 1.0f, 0.0f,
        0.5f, 0.5f, 0.0f, 0.2f, 0.8f, 0.2f, 1.0f, 1.0f, 1.0f,
        -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f
    )

    override fun getLayout(): BufferLayout = BufferLayout(
        listOf(
            BufferElement("a_Position", ShaderDataType.Float3, true),
            BufferElement("a_Color", ShaderDataType.Float4, false),
            BufferElement("a_TexCoord", ShaderDataType.Float2, true)
        )
    )

    override fun getIndices(): ShortArray = shortArrayOf(
        0, 1, 2, 2, 3, 0
    )

    override fun beforeBuild() {
    }

    override fun afterBuild() {
    }
}