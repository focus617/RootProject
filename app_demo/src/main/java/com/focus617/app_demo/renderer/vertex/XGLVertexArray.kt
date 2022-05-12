package com.focus617.app_demo.renderer.vertex

import android.opengl.GLES20.*
import android.opengl.GLES31
import com.focus617.core.engine.objects.IfDrawable
import com.focus617.core.engine.renderer.IfBuffer
import com.focus617.core.engine.renderer.vertex.ShaderDataType
import com.focus617.core.engine.renderer.vertex.VertexArray
import com.focus617.mylib.logging.WithLogging
import java.io.Closeable
import java.nio.IntBuffer

/**
 * VertexArray is used to link VertexBuffer with data layout
 */
class XGLVertexArray : VertexArray(), IfBuffer, Closeable {
    // Vertex Array handle
    private var mHandle: Int = 0
    private var mVAOBuf: IntBuffer = IntBuffer.allocate(1)

    // Vertex Buffer stack
    private var mVertexBufferStack = mutableListOf<XGLVertexBuffer>()

    // Index/Element Buffer handle
    private var mIndexBuffer: XGLIndexBuffer? = null

    init {
        // Generate VAO ID(顶点数组对象)
        GLES31.glGenVertexArrays(1, mVAOBuf)
        if (mVAOBuf.get(0) == 0) {
            throw RuntimeException("Could not create a new vertex array object.")
        }

        mHandle = mVAOBuf.get(0)
    }

    override fun close() {
        glDeleteBuffers(1, mVAOBuf)
    }

    override fun bind() {
        GLES31.glBindVertexArray(mHandle)
    }

    override fun unbind() {
        GLES31.glBindVertexArray(0)
    }

    fun addVertexBuffer(vertexBuffer: XGLVertexBuffer): Boolean {
        val layout = vertexBuffer.getLayout()
        val elements = vertexBuffer.getLayout()?.getElements()
        if ((layout == null) || (elements == null) || (elements.isEmpty())) {
            LOG.warn("Vertex Buffer has no layout!")
            return false
        }

        GLES31.glBindVertexArray(mHandle)
        vertexBuffer.bind()

        for ((index, element) in elements.withIndex()) {
            // 启用顶点属性
            glEnableVertexAttribArray(index)
            // 设置顶点属性
            glVertexAttribPointer(
                index,
                element.type.getComponentCount(),
                shaderDataTypeToOpenGLBaseType(element.type),
                element.normalized,
                layout.getStride(),
                element.offset
            )
        }
        mVertexBufferStack.add(vertexBuffer)
        return true
    }

    fun setIndexBuffer(indexBuffer: XGLIndexBuffer) {
        GLES31.glBindVertexArray(mHandle)
        indexBuffer.bind()
        mIndexBuffer = indexBuffer
    }

    fun getIndexBuffer() = mIndexBuffer

    companion object : WithLogging() {

        fun shaderDataTypeToOpenGLBaseType(type: ShaderDataType): Int = when (type) {
            ShaderDataType.Float1 -> GL_FLOAT
            ShaderDataType.Float2 -> GL_FLOAT
            ShaderDataType.Float3 -> GL_FLOAT
            ShaderDataType.Float4 -> GL_FLOAT
            ShaderDataType.Mat3 -> GL_FLOAT
            ShaderDataType.Mat4 -> GL_FLOAT
            ShaderDataType.Int1 -> GL_INT
            ShaderDataType.Int2 -> GL_INT
            ShaderDataType.Int3 -> GL_INT
            ShaderDataType.Int4 -> GL_INT
            ShaderDataType.Bool -> GL_BOOL

            else -> {
                LOG.error("Unknown ShaderDataType!")
                0
            }
        }

        fun buildVertexArray(drawingObject: IfDrawable): XGLVertexArray {
            val vertexArray =
                XGLVertexBufferBuilder.createVertexArray() as XGLVertexArray

            // let drawingObject prepare data
            drawingObject.beforeBuild()

            val vertices = drawingObject.getVertices()
            val vertexBuffer = XGLVertexBufferBuilder.createVertexBuffer(
                vertices, vertices.size * Float.SIZE_BYTES
            ) as XGLVertexBuffer
            vertexBuffer.setLayout(drawingObject.getLayout())
            vertexArray.addVertexBuffer(vertexBuffer)

            val indices = drawingObject.getIndices()
            val indexBuffer = XGLVertexBufferBuilder.createIndexBuffer(
                indices, indices.size
            ) as XGLIndexBuffer
            vertexArray.setIndexBuffer(indexBuffer)

            // let drawingObject release data buffer
            drawingObject.afterBuild()

            return vertexArray
        }

    }
}