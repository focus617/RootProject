package com.focus617.app_demo.renderer

import android.opengl.GLES20
import android.opengl.GLES31
import com.focus617.core.engine.renderer.IfBuffer
import com.focus617.core.platform.base.BaseEntity
import java.io.Closeable
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer
import java.nio.ShortBuffer

class XGLIndexBuffer(indices: ShortArray, count: Int) : BaseEntity(), IfBuffer, Closeable {
    // Index/Element Buffer handle
    private var mHandle: Int = 0
    private var mVBOBuf: IntBuffer = IntBuffer.allocate(1)

    // Index count in buffer
    var mCount = count

    init {
        // Generate VBO ID
        GLES31.glGenBuffers(mVBOBuf.capacity(), mVBOBuf)
        if (mVBOBuf.get(0) == 0) {
            throw RuntimeException("Could not create a new vertex buffer object.")
        }

        mHandle = mVBOBuf.get(0)

        // Bind VBO buffer
//        GLES31.glBindBuffer(GLES31.GL_ELEMENT_ARRAY_BUFFER, mHandle)
//
        // Transfer data from native memory to the GPU buffer.
//        GLES31.glBufferData(
//            GLES31.GL_ELEMENT_ARRAY_BUFFER,
//            count * Short.SIZE_BYTES,
//            ShortBuffer.wrap(indices),
//            GLES31.GL_STATIC_DRAW
//        )

        // GL_ELEMENT_ARRAY_BUFFER is not valid without an actively bound VAO
        // Binding with GL_ARRAY_BUFFER allows the data to be loaded regardless of VAO state.
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, mHandle)

        // Transfer data from native memory to the GPU buffer.
        GLES31.glBufferData(
            GLES31.GL_ARRAY_BUFFER,
            count * Short.SIZE_BYTES,
            ShortBuffer.wrap(indices),
            GLES31.GL_STATIC_DRAW
        )
    }

    override fun close() {
        GLES20.glDeleteBuffers(1, mVBOBuf)
    }

    override fun bind() {
        GLES20.glBindBuffer(GLES31.GL_ELEMENT_ARRAY_BUFFER, mHandle)
    }

    override fun unbind() {
        GLES20.glBindBuffer(GLES31.GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    // 提示：由于不同平台字节顺序不同，数据单元不是字节的一定要经过ByteBuffer转换，
    // 关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
    private fun shortBuffer(indices: ShortArray): ShortBuffer = ByteBuffer
        .allocateDirect(indices.size * Short.SIZE_BYTES)
        .order(ByteOrder.nativeOrder())         //设置字节顺序为本地操作系统顺序
        .asShortBuffer()                        //转换为Short型缓冲
        .put(indices)                           //在缓冲区内写入数据

}

