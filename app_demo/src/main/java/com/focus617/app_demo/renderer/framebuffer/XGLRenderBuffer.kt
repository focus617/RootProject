package com.focus617.app_demo.renderer.framebuffer

import android.opengl.GLES31.*
import com.focus617.core.platform.base.BaseEntity
import java.io.Closeable
import java.nio.IntBuffer

class XGLRenderBuffer private constructor() : BaseEntity(), Closeable {
    private var mRenderBuf: IntBuffer = IntBuffer.allocate(1)
    var mHandle: Int = -1   // RenderBuffer的Handle

    // 采用GL_DEPTH24_STENCIL8作为内部格式，它同时代表24位的深度和8位的模板缓冲
    constructor(width: Int, height: Int, format: Int) : this() {
        // Generate RBO
        glGenRenderbuffers(1, mRenderBuf)
        if (mRenderBuf[0] == 0) {
            throw RuntimeException("Could not create a new render buffer object.")
        }
        mHandle = mRenderBuf[0]
        glBindRenderbuffer(GL_RENDERBUFFER, mHandle)

        glRenderbufferStorage(GL_RENDERBUFFER, format, width, height)
        glBindRenderbuffer(GL_RENDERBUFFER, 0)
    }

    // Renderbuffer for multisampling
    constructor(width: Int, height: Int) : this() {
        // Generate RBO
        glGenRenderbuffers(1, mRenderBuf)
        if (mRenderBuf[0] == 0) {
            throw RuntimeException("Could not create a new render buffer object.")
        }
        mHandle = mRenderBuf[0]
        glBindRenderbuffer(GL_RENDERBUFFER, mHandle)
        glRenderbufferStorageMultisample(GL_RENDERBUFFER, 4, GL_RGBA8, width, height)
        glBindRenderbuffer(GL_RENDERBUFFER, 0)

        /* 使用方法
        // 1.Bind with Framebuffer
        glBindFramebuffer(GL_FRAMEBUFFER, fbo)
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, rbo.mHandle)

        // 2.Then render:
        glBindFramebuffer(GL_READ_FRAMEBUFFER, fbo)
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0)
        glBlitFramebuffer(0, 0, mScreenWidth, mScreenHeight, 0, 0, mScreenWidth, mScreenHeight,
                      GL_COLOR_BUFFER_BIT, GL_NEAREST)
         */
    }

    override fun close() {
        glDeleteRenderbuffers(1, mRenderBuf)
    }

    fun bind() {
        glBindRenderbuffer(GL_RENDERBUFFER, mHandle)
        // 在正常绘制时确保关闭模板缓冲的写入
        glStencilMask(0x00)
    }

    fun unbind() {
        glBindRenderbuffer(GL_RENDERBUFFER, 0)
    }


}