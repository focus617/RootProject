package com.focus617.app_demo.framebuffer

import android.opengl.GLES31.*
import com.focus617.core.platform.base.BaseEntity
import java.io.Closeable
import java.nio.IntBuffer

class XGLRenderBuffer(width: Int, height: Int) : BaseEntity(), Closeable {
    private var mRenderBuf: IntBuffer = IntBuffer.allocate(1)
    var mHandle: Int = -1   // RenderBuffer的Handle

    init {
        // Generate RBO
        glGenRenderbuffers(1, mRenderBuf)
        if (mRenderBuf[0] == 0) {
            throw RuntimeException("Could not create a new render buffer object.")
        }
        mHandle = mRenderBuf[0]
        glBindRenderbuffer(GL_RENDERBUFFER, mHandle)
        // 采用GL_DEPTH24_STENCIL8作为内部格式，它同时代表24位的深度和8位的模板缓冲
        glRenderbufferStorage(
            GL_RENDERBUFFER,
            GL_DEPTH24_STENCIL8,
            width,
            height
        )
        glBindRenderbuffer(GL_RENDERBUFFER, 0)
    }

    override fun close() {
        glDeleteRenderbuffers(1, mRenderBuf)
    }

    fun bind() {
        glBindRenderbuffer(GL_RENDERBUFFER, mHandle)

        // 开启模板测试，设置模板、深度测试通过或失败时才采取动作
        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE)

        // 在正常绘制时确保关闭模板缓冲的写入
        glStencilMask(0x00)
    }

    fun unbind() {
        glBindRenderbuffer(GL_RENDERBUFFER, 0)
    }

}