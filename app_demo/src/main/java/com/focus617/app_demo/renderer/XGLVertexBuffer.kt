package com.focus617.app_demo.renderer

import android.opengl.GLES20.GL_ARRAY_BUFFER
import android.opengl.GLES20.glBindBuffer
import android.opengl.GLES31
import com.focus617.core.engine.renderer.IfBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

class XGLVertexBuffer(vertices: FloatArray, size: Int) : IfBuffer {
    // Vertex Buffer handle
    private var mVertexBufID: Int = 0

    init {
        // Generate VBO ID
        val mVBOBuf = IntBuffer.allocate(1)   // 顶点缓存对象
        GLES31.glGenBuffers(1, mVBOBuf)
        if (mVBOBuf.get(0) == 0) {
            throw RuntimeException("Could not create a new vertex buffer object.")
        }

        mVertexBufID = mVBOBuf.get(0)

        // Bind VBO buffer
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, mVertexBufID)

        // Transfer data from native memory to the GPU buffer.
        GLES31.glBufferData(
            GLES31.GL_ARRAY_BUFFER, size, FloatBuffer.wrap(vertices), GLES31.GL_STATIC_DRAW
        )
    }

    override fun bind() {
        glBindBuffer(GL_ARRAY_BUFFER, mVertexBufID)
    }

    override fun unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    // 提示：由于不同平台字节顺序不同，数据单元不是字节的一定要经过ByteBuffer转换，
    // 关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
    private fun floatBuffer(vertices: FloatArray): FloatBuffer = ByteBuffer
        .allocateDirect(vertices.size * Float.SIZE_BYTES)
        .order(ByteOrder.nativeOrder())         //设置字节顺序为本地操作系统顺序
        .asFloatBuffer()                        //转换为浮点(Float)型缓冲
        .put(vertices)                          //在缓冲区内写入数据

}
