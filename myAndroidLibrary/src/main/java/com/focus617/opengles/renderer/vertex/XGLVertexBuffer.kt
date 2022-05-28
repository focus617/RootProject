package com.focus617.opengles.renderer.vertex

import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.GLES31
import com.focus617.core.engine.renderer.api.IfBuffer
import com.focus617.core.engine.renderer.api.IfBufferLayout
import com.focus617.core.engine.renderer.vertex.BufferLayout
import com.focus617.core.platform.base.BaseEntity
import java.io.Closeable
import java.nio.*

class XGLVertexBuffer private constructor() : BaseEntity(), IfBuffer, IfBufferLayout, Closeable {
    private var mVBOBuf: IntBuffer = IntBuffer.allocate(1)

    private var mHandle: Int = 0                // Vertex Buffer handle
    private var mLayout: BufferLayout? = null   // Vertex Buffer layout

    constructor(vertices: FloatArray, size: Int) : this() {
        // Generate VBO ID
        GLES31.glGenBuffers(1, mVBOBuf)
        if (mVBOBuf.get(0) == 0) {
            throw RuntimeException("Could not create a new vertex buffer object.")
        }

        mHandle = mVBOBuf.get(0)

        // Bind VBO buffer
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, mHandle)

        // Transfer data from native memory to the GPU buffer.
        GLES31.glBufferData(
            GLES31.GL_ARRAY_BUFFER, size, FloatBuffer.wrap(vertices), GLES31.GL_STATIC_DRAW
        )
    }

    constructor(size: Int) : this() {
        // Generate VBO ID
        GLES31.glGenBuffers(1, mVBOBuf)
        if (mVBOBuf.get(0) == 0) {
            throw RuntimeException("Could not create a new vertex buffer object.")
        }

        mHandle = mVBOBuf.get(0)

        // Bind VBO buffer
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, mHandle)

        // Transfer data from native memory to the GPU buffer.
        GLES31.glBufferData(
            GLES31.GL_ARRAY_BUFFER, size, null, GLES31.GL_DYNAMIC_DRAW
        )
    }

    override fun close() {
        GLES20.glDeleteBuffers(1, mVBOBuf)
    }

    override fun bind() {
        glBindBuffer(GL_ARRAY_BUFFER, mHandle)
    }

    override fun unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    fun setData(data: Buffer, size: Int) {
        glBindBuffer(GL_ARRAY_BUFFER, mHandle)
        glBufferSubData(GL_ARRAY_BUFFER, 0, size, data)
    }

    override fun getLayout(): BufferLayout? = mLayout

    override fun setLayout(layout: BufferLayout) {
        mLayout = layout
    }

    // 提示：由于不同平台字节顺序不同，数据单元不是字节的一定要经过ByteBuffer转换，
    // 关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
    private fun floatBuffer(vertices: FloatArray): FloatBuffer = ByteBuffer
        .allocateDirect(vertices.size * Float.SIZE_BYTES)
        .order(ByteOrder.nativeOrder())         //设置字节顺序为本地操作系统顺序
        .asFloatBuffer()                        //转换为浮点(Float)型缓冲
        .put(vertices)                          //在缓冲区内写入数据


}
