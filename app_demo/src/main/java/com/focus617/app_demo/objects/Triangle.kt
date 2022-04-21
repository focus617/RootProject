package com.focus617.app_demo.objects

import android.opengl.GLES31
import com.focus617.app_demo.engine.XGLRenderer
import com.focus617.app_demo.renderer.*
import com.focus617.core.engine.renderer.BufferElement
import com.focus617.core.engine.renderer.BufferLayout
import com.focus617.core.engine.renderer.ShaderDataType
import kotlin.math.sin


class Triangle : DrawingObject() {

    private val U_COLOR = "u_Color"

    // 定义顶点着色器
    // [mMVPMatrix] 模型视图投影矩阵
    private val vertexShaderCode =
        "#version 300 es \n" +
                "layout (location = 0) in vec3 aPos;" +
                "uniform mat4 uMVPMatrix;" +
                "void main() {" +
                "   gl_Position = uMVPMatrix * vec4(aPos.x, aPos.y, aPos.z, 1.0);" +
                "}"

    // 定义片段着色器
    private val fragmentShaderCode =
        "#version 300 es \n " +
                "#ifdef GL_ES\n" +
                "precision highp float;\n" +
                "#endif\n" +

                "out vec4 FragColor; " +
                "uniform vec4 u_Color; " +

                "void main() {" +
                "  FragColor = u_Color;" +
                "}"


    private val shader = XGLShader(vertexShaderCode, fragmentShaderCode)
    private val vertexArray = XGLBufferBuilder.createVertexArray() as XGLVertexArray


    init {
        setupVertices()
        setupIndices()
    }

    private fun setupVertices() {
        val vertexBuffer = XGLBufferBuilder.createVertexBuffer(
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
        val indexBuffer = XGLBufferBuilder.createIndexBuffer(
            indices, indices.size
        ) as XGLIndexBuffer

        vertexArray.setIndexBuffer(indexBuffer)
    }

    fun draw(renderer: XGLRenderer, mvpMatrix: FloatArray) {

        shader.bind()

        // 获取模型视图投影矩阵的句柄
        val mMVPMatrixHandle = GLES31.glGetUniformLocation(shader.mRendererId, "uMVPMatrix")
        // 将模型视图投影矩阵传递给顶点着色器
        GLES31.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)

        // 设置片元着色器使用的颜色
        setupColor(blink = true)

        renderer.submit(vertexArray)

        shader.unbind()

    }

    private fun setupColor(blink: Boolean = false) {

        // 查询 uniform ourColor的位置值
        val fragmentColorLocation = GLES31.glGetUniformLocation(shader.mRendererId, U_COLOR)
        if (blink) {
            // 使用sin函数让颜色随时间在0.0到1.0之间改变
            val timeValue = System.currentTimeMillis()
            val greenValue = sin((timeValue / 300 % 50).toDouble()) / 2 + 0.5
            GLES31.glUniform4f(fragmentColorLocation, greenValue.toFloat(), 0.5f, 0.2f, 1.0f)
        } else {
            GLES31.glUniform4f(fragmentColorLocation, 1.0f, 0.5f, 0.2f, 1.0f)
        }
    }

    // 顶点数据集，及其属性
    companion object {
        private const val UNIT_SIZE = 0.20f

        // 假定每个顶点有2个顶点属性一位置、颜色
        val vertices = floatArrayOf(
            // x,   y,  z,  R,  G,  B,  Alpha
            0.0f, 0.622008459f, 0.0f, 1f, 1f, 1f, 0f,   // 上
            -0.5f, -0.311004243f, 0.0f, 0f, 0f, 1f, 0f,   // 左下
            0.5f, -0.311004243f, 0.0f, 0f, 1f, 0f, 0f    // 右下
        )

        internal var indices = shortArrayOf(
            0, 1, 2
        )

    }
}