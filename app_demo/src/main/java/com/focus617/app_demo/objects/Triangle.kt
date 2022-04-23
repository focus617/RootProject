package com.focus617.app_demo.objects

import com.focus617.app_demo.engine.XGLShaderBuilder
import com.focus617.app_demo.renderer.XGLIndexBuffer
import com.focus617.app_demo.renderer.XGLVertexArray
import com.focus617.app_demo.renderer.XGLVertexBuffer
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.engine.renderer.BufferElement
import com.focus617.core.engine.renderer.BufferLayout
import com.focus617.core.engine.renderer.ShaderDataType


class Triangle : DrawingObject() {

    val vertexArray = XGLShaderBuilder.createVertexArray() as XGLVertexArray

    var transform: FloatArray = FloatArray(16)

    init {
        setupVertices()
        setupIndices()
        XMatrix.setIdentityM(transform, 0)
    }

    private fun setupVertices() {
        val vertexBuffer = XGLShaderBuilder.createVertexBuffer(
            vertices, vertices.size * Float.SIZE_BYTES
        ) as XGLVertexBuffer

        val layout = BufferLayout(
            listOf(
                BufferElement("a_position", ShaderDataType.Float3, true),
                BufferElement("a_color", ShaderDataType.Float4, true)
            )
        )
        vertexBuffer.setLayout(layout)
        vertexArray.addVertexBuffer(vertexBuffer)
    }

    private fun setupIndices() {
        val indexBuffer = XGLShaderBuilder.createIndexBuffer(
            indices, indices.size
        ) as XGLIndexBuffer

        vertexArray.setIndexBuffer(indexBuffer)
    }

    // 顶点数据集，及其属性
    companion object {
        private const val UNIT_SIZE = 0.20f

        // 假定每个顶点有2个顶点属性一位置、颜色
        val vertices = floatArrayOf(
            // x,   y,  z,  R,  G,  B,  Alpha
            0.0f, 0.622008459f, 0.0f, 1f, 0f, 0f, 0f,     // 上
            -0.8f, -0.311004243f, 0.0f, 0f, 0f, 1f, 0f,   // 左下
            0.8f, -0.311004243f, 0.0f, 0f, 1f, 0f, 0f     // 右下
        )

        internal var indices = shortArrayOf(
            0, 1, 2
        )

    }
}