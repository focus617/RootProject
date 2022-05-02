package com.focus617.core.engine.objects.d2

import com.focus617.core.engine.objects.DrawableObject
import com.focus617.core.engine.renderer.BufferElement
import com.focus617.core.engine.renderer.BufferLayout
import com.focus617.core.engine.renderer.ShaderDataType

class Triangle : DrawableObject() {

    override fun getVertices(): FloatArray = floatArrayOf(
        // 每个顶点有2个顶点属性一位置、颜色、纹理（TODO： TextureId）
        // x,   y,          z,    color.r,.b,.g,.a, TextureX, TextureY
        0.0f, 0.622008459f, 0.0f, 1f, 0f, 0f, 0f, 0.5f, 1.0f,    // 上
        -0.8f, -0.311004243f, 0.0f, 0f, 0f, 1f, 0f, 0.0f, 0.0f,  // 左下
        0.8f, -0.311004243f, 0.0f, 0f, 1f, 0f, 0f, 1.0f, 0.0f    // 右下
    )

    override fun getLayout(): BufferLayout = BufferLayout(
        listOf(
            BufferElement("a_Position", ShaderDataType.Float3, true),
            BufferElement("a_Color", ShaderDataType.Float4, false),
            BufferElement("a_TexCoord", ShaderDataType.Float2, true)
        )
    )

    override fun getIndices(): ShortArray = shortArrayOf(
        0, 1, 2
    )

    override fun beforeBuild() {
    }

    override fun afterBuild() {
    }
}