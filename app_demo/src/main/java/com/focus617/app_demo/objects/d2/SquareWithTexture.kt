package com.focus617.app_demo.objects.d2

import com.focus617.app_demo.renderer.XGLBufferBuilder
import com.focus617.app_demo.renderer.XGLIndexBuffer
import com.focus617.app_demo.renderer.XGLVertexArray
import com.focus617.app_demo.renderer.XGLVertexBuffer
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.engine.renderer.BufferElement
import com.focus617.core.engine.renderer.BufferLayout
import com.focus617.core.engine.renderer.ShaderDataType


class Square {

    val vertexArray = XGLBufferBuilder.createVertexArray() as XGLVertexArray

    var transform: FloatArray = FloatArray(16)

    init {
        setupVertices()
        setupIndices()
        XMatrix.setIdentityM(transform, 0)
    }

    private fun setupVertices() {
        val vertexBuffer = XGLBufferBuilder.createVertexBuffer(
            vertices, vertices.size * Float.SIZE_BYTES
        ) as XGLVertexBuffer

        val layout = BufferLayout(
            listOf(
                BufferElement("a_Position", ShaderDataType.Float3, true),
                BufferElement("a_TexCoord", ShaderDataType.Float2, true)
            )
        )
        vertexBuffer.setLayout(layout)
        vertexArray.addVertexBuffer(vertexBuffer)
    }

    private fun setupIndices() {
        val indexBuffer = XGLBufferBuilder.createIndexBuffer(
            indices, indices.size
        ) as XGLIndexBuffer

        vertexArray.setIndexBuffer(indexBuffer)
    }

    // 顶点数据集，及其属性
    companion object {
        private const val UNIT_SIZE = 0.20f

        // 每个顶点有2个顶点属性一位置、纹理
        val vertices = floatArrayOf(
            // x,   y,     z,  TextureX, TextureY
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
            0.5f,  0.5f, 0.0f, 1.0f, 1.0f,
            -0.5f,  0.5f, 0.0f, 0.0f, 1.0f
        )

        internal var indices = shortArrayOf(
            0, 1, 2, 2, 3, 0
        )

    }
}