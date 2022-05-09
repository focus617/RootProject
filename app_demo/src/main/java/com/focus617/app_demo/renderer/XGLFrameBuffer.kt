package com.focus617.app_demo.renderer

import android.opengl.GLES20.GL_FRAMEBUFFER
import android.opengl.GLES31
import com.focus617.core.engine.renderer.Framebuffer
import java.nio.IntBuffer


class XGLFrameBuffer(width: Int, height: Int) : Framebuffer(width, height) {
    private var mFrameBuf: IntBuffer = IntBuffer.allocate(1)

    private var mHandle: Int = -1
    private lateinit var mColorAttachmentTexture2D: XGLTexture2D

    init {
        // Generate FrameBuffer ID
        GLES31.glGenBuffers(1, mFrameBuf)
        if (mFrameBuf.get(0) == 0) {
            throw RuntimeException("Could not create a new vertex buffer object.")
        }
        mHandle = mFrameBuf.get(0)
        // Bind FrameBuffer
        GLES31.glBindBuffer(GLES31.GL_FRAMEBUFFER, mHandle)

        // 创建Texture2D，作为FrameBuffer的output image
        mColorAttachmentTexture2D= XGLTexture2D(this, mWidth, mHeight)

        // attach it to the frame buffer, 作为输出的texture
        GLES31.glFramebufferTexture2D(
            GLES31.GL_FRAMEBUFFER,
            GLES31.GL_COLOR_ATTACHMENT0,
            GLES31.GL_TEXTURE_2D,
            mColorAttachmentTexture2D.mHandle,
            0
        )

        // Stencil和Depth Buffer的Attachment暂时不加了

        val state: Boolean =
            (GLES31.glCheckFramebufferStatus(GL_FRAMEBUFFER) == GLES31.GL_FRAMEBUFFER_COMPLETE)
        check(state){
            LOG.warn("Framebuffer is incomplete!")
        }
    }

    override fun close() {
        GLES31.glDeleteFramebuffers(1, mFrameBuf)
        mColorAttachmentTexture2D.close()
    }

    override fun bind() {
        GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, mHandle)
    }

    override fun unbind() {
        GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, 0)
    }

    override fun getColorAttachmentTextureId() = mColorAttachmentTexture2D.mHandle

    override fun resizeColorAttachment(width: Int, height: Int) {
        TODO("Not yet implemented")
    }
}