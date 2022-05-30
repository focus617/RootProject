package com.focus617.opengles.renderer.framebuffer

import android.opengl.GLES31.*
import com.focus617.core.engine.renderer.framebuffer.IfFrameBufferAttachment
import com.focus617.core.platform.base.BaseEntity
import java.io.Closeable
import java.nio.IntBuffer

class XGLRenderBuffer private constructor() : BaseEntity(), Closeable, IfFrameBufferAttachment {
    private var mRenderBuf: IntBuffer = IntBuffer.allocate(1)
    override var mHandle: Int = -1   // RenderBuffer的Handle

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

    /**
    opengl es not support for texture multisampling, glTexStorage2DMultisample not work for
    texture multisampling. opengl es only support renderbuffer for multisampling.

    使用方法
    // 1.Bind with Framebuffer
    glBindFramebuffer(GL_FRAMEBUFFER, fbo)
    glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, rbo.mHandle)

    // 2.Then render:
    glBindFramebuffer(GL_READ_FRAMEBUFFER, fbo)
    glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0)
    glBlitFramebuffer(0, 0, mScreenWidth, mScreenHeight, 0, 0, mScreenWidth, mScreenHeight,
    GL_COLOR_BUFFER_BIT, GL_NEAREST)
     */
    // Renderbuffer for multisampling
    constructor(width: Int, height: Int, samples: Int, format: Int = GL_RGBA8) : this() {
        // Generate RBO
        glGenRenderbuffers(1, mRenderBuf)
        if (mRenderBuf[0] == 0) {
            throw RuntimeException("Could not create a new render buffer object.")
        }
        mHandle = mRenderBuf[0]

        glBindRenderbuffer(GL_RENDERBUFFER, mHandle)
        glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples, format, width, height)
        glBindRenderbuffer(GL_RENDERBUFFER, 0)
    }

    override fun close() {
        glDeleteRenderbuffers(1, mRenderBuf)
    }

    override fun bind() {
        glBindRenderbuffer(GL_RENDERBUFFER, mHandle)
        // 在正常绘制时确保关闭模板缓冲的写入
        glStencilMask(0x00)
    }

    override fun unbind() {
        glBindRenderbuffer(GL_RENDERBUFFER, 0)
    }


}