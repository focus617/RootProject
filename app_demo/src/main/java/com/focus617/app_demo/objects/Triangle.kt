package com.focus617.app_demo.objects

import android.opengl.GLES31
import com.focus617.app_demo.renderer.XGLBufferBuilder
import com.focus617.app_demo.renderer.XGLShader
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
    private val vertexBuffer =
        XGLBufferBuilder.createVertexBuffer(triangleCoords, triangleCoords.size * Float.SIZE_BYTES)
    private val indexBuffer =
        XGLBufferBuilder.createIndexBuffer(indices, indices.size)

    init {
        // 启用顶点数组
        GLES31.glEnableVertexAttribArray(VERTEX_POS_INDEX)

        // 链接顶点属性，告诉OpenGL该如何解析顶点数据
        // 目前只有一个顶点位置属性
        GLES31.glVertexAttribPointer(
            VERTEX_POS_INDEX,
            VERTEX_POS_SIZE,
            GLES31.GL_FLOAT,
            false,
            VERTEX_STRIDE,
            VERTEX_POS_OFFSET
        )
    }

    fun draw(mvpMatrix: FloatArray) {

        // 将程序添加到OpenGL ES环境
        shader.bind()

        // 获取模型视图投影矩阵的句柄
        val mMVPMatrixHandle = GLES31.glGetUniformLocation(shader.mRendererId, "uMVPMatrix")
        // 将模型视图投影矩阵传递给顶点着色器
        GLES31.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)

        // 设置片元着色器使用的颜色
        setupColor(blink = true)

        vertexBuffer!!.bind()

        // 图元装配，绘制三角形
        //GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, vertexCount)
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, indices.size, GLES31.GL_UNSIGNED_SHORT, 0)

        vertexBuffer!!.unbind()

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
        // 假定每个顶点有4个顶点属性一位置、法线和两个纹理坐标

        // 顶点坐标的每个属性的Size
        internal const val VERTEX_POS_SIZE = 3          //x,y,and z
        internal const val VERTEX_NORMAL_SIZE = 3       //x,y,and z
        internal const val VERTEX_TEXCOORDO_SIZE = 2    //s and t
        internal const val VERTEX_TEXCOORD1_SIZE = 2    //s and t

        // 顶点坐标的每个属性的Index
        internal const val VERTEX_POS_INDEX = 0
        internal const val VERTEX_NORMAL_INDEX = 1
        internal const val VERTEX_TEXCOORDO_INDEX = 2
        internal const val VERTEX_TEXCOORD1_INDEX = 3

        // the following 4 defines are used to determine the locations
        // of various attributes if vertex data are stored as an array
        //of structures
        internal const val VERTEX_POS_OFFSET = 0
        internal const val VERTEX_NORMAL_OFFSET = 3
        internal const val VERTEX_TEX_COORDO_OFFSET = 6
        internal const val VERTEX_TEX_COORD1_OFFSET = 8

        internal const val VERTEX_ATTRIBUTE_SIZE = VERTEX_POS_SIZE
        // (VERTEX_POS_SIZE+ VERTEX_NORMAL_SIZE+ VERTEX_TEXCOORDO_SIZE+ VERTEX_TEXCOORD1_SIZE)

        // 一个等边三角形的顶点输入
        internal var triangleCoords = floatArrayOf(  // 按逆时针顺序
            0.0f, 0.622008459f, 0.0f,   // 上
            -0.5f, -0.311004243f, 0.0f, // 左下
            0.5f, -0.311004243f, 0.0f   // 右下
        )

        internal var indices = shortArrayOf(
            0, 1, 2
        )

        // 顶点的数量
        internal val vertexCount = triangleCoords.size / VERTEX_ATTRIBUTE_SIZE

        // 连续的顶点属性组之间的间隔
        internal const val VERTEX_STRIDE = VERTEX_ATTRIBUTE_SIZE * Float.SIZE_BYTES
    }
}