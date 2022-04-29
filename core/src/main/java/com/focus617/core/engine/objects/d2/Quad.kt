package com.focus617.core.engine.objects.d2

import com.focus617.core.engine.objects.DrawableObject
import com.focus617.core.engine.objects.IfDrawable
import com.focus617.core.engine.renderer.BufferElement
import com.focus617.core.engine.renderer.BufferLayout
import com.focus617.core.engine.renderer.ShaderDataType

class Quad : DrawableObject() {

    companion object : IfDrawable {
         override fun getVertices(): FloatArray = floatArrayOf(
            // 每个顶点有2个顶点属性一位置、纹理
            // x,   y,     z,  TextureX, TextureY
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.0f, 1.0f, 1.0f,
            -0.5f, 0.5f, 0.0f, 0.0f, 1.0f
        )

        override fun getLayout(): BufferLayout = BufferLayout(
            listOf(
                BufferElement("a_Position", ShaderDataType.Float3, true),
                BufferElement("a_TexCoord", ShaderDataType.Float2, true)
            )
        )

        override fun getIndices(): ShortArray = shortArrayOf(
            0, 1, 2, 2, 3, 0
        )
    }
}