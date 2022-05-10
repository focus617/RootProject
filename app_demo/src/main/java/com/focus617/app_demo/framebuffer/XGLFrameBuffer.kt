package com.focus617.app_demo.framebuffer

import android.opengl.GLES31.*
import com.focus617.app_demo.renderer.XGLTexture2D
import com.focus617.app_demo.renderer.XGLTextureSlots
import com.focus617.core.engine.renderer.Framebuffer
import java.nio.IntBuffer


class XGLFrameBuffer(width: Int, height: Int) : Framebuffer(width, height) {
    private var mFrameBuf: IntBuffer = IntBuffer.allocate(1)
    private var mRenderBuf: IntBuffer = IntBuffer.allocate(1)

    private var mFBOHandle: Int = -1   // FrameBuffer的Handle
    private var mRBOHandle: Int = -1      // RenderBuffer的Handle

    private var mColorAttachmentTexture2D: XGLTexture2D // ColorTexture的Handle
    private var mColorAttachmentTextureIndex: Int = -1  // 在TextureSlots内的Index

    init {
        // Generate FBO
        glGenFramebuffers(1, mFrameBuf)
        if (mFrameBuf[0] == 0) {
            throw RuntimeException("Could not create a new frame buffer object.")
        }
        mFBOHandle = mFrameBuf[0]

        // 创建Texture2D，作为FrameBuffer的Color Attachment
        // 把纹理的维度设置为屏幕大小：传入width和height，只分配相应的内存，而不填充
        mColorAttachmentTexture2D = XGLTexture2D(this, mWidth, mHeight)
        // 注册到TextureSlots, 以便ActiveTexture
        mColorAttachmentTextureIndex = XGLTextureSlots.getId(mColorAttachmentTexture2D)

        // Bind FrameBuffer
        glBindFramebuffer(GL_FRAMEBUFFER, mFBOHandle)
        // attach Texture2D to the FrameBuffer, 作为输出的texture
        glFramebufferTexture2D(
            GL_FRAMEBUFFER,
            GL_COLOR_ATTACHMENT0,
            GL_TEXTURE_2D,
            mColorAttachmentTexture2D.mHandle,
            0
        )

        // Generate RBO for FrameBuffer's Stencil and Depth Attachment
        glGenRenderbuffers(1, mRenderBuf)
        if (mRenderBuf[0] == 0) {
            throw RuntimeException("Could not create a new render buffer object.")
        }
        mRBOHandle = mRenderBuf[0]

        glBindRenderbuffer(GL_RENDERBUFFER, mRBOHandle)
        // 采用GL_DEPTH24_STENCIL8作为内部格式，它同时代表24位的深度和8位的模板缓冲
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, mWidth, mHeight)
        // Attach the Renderbuffer to FrameBuffer
        glFramebufferRenderbuffer(
            GL_FRAMEBUFFER,
            GL_DEPTH_STENCIL_ATTACHMENT,
            GL_RENDERBUFFER,
            mRBOHandle
        )
        glBindRenderbuffer(GL_RENDERBUFFER, 0)

        val state: Boolean = (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE)
        check(state) { LOG.warn("Framebuffer is incomplete!") }

        glBindTexture(GL_TEXTURE_2D,0)
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    override fun close() {
        glDeleteFramebuffers(1, mFrameBuf)
        mColorAttachmentTexture2D.close()
    }

    override fun bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, mFBOHandle)
    }

    override fun unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    override fun getColorAttachmentTextureId() = mColorAttachmentTexture2D.mHandle

    override fun resizeColorAttachment(width: Int, height: Int) {
        if (mFBOHandle != -1) {
            mWidth = width
            mHeight = height

            // 注意, 这里不需要BindFramebuffer
            glBindTexture(GL_TEXTURE_2D, mColorAttachmentTexture2D.mHandle)
            glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                width,
                height,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                null
            )

        }
    }
}