package com.focus617.app_demo.framebuffer

import android.opengl.GLES20.GL_FRAMEBUFFER
import android.opengl.GLES20.glBindFramebuffer
import android.opengl.GLES31.*
import com.focus617.app_demo.engine.XGLContext
import com.focus617.app_demo.renderer.XGLTexture2D
import com.focus617.app_demo.renderer.XGLTextureSlots
import com.focus617.core.engine.renderer.Framebuffer
import java.nio.IntBuffer


class XGLFrameBuffer(
    width: Int = 1080,
    height: Int = 2220
) : Framebuffer(width, height) {
    private var mFrameBuf: IntBuffer = IntBuffer.allocate(1)
    private var mFBOHandle: Int = -1   // FrameBuffer的Handle

    private var mColorAttachmentTexture2D: XGLTexture2D // ColorTextureBuffer
    private var mColorAttachmentTextureIndex: Int = -1  // 在TextureSlots内的Index

    private var mRenderBuf: XGLRenderBuffer // RenderBuffer

    private var mQuad: FrameBufferQuad

    init {
        LOG.info("XGLFrameBuffer created.")

        // Generate FBO
        glGenFramebuffers(1, mFrameBuf)
        if (mFrameBuf[0] == 0) {
            throw RuntimeException("Could not create a new frame buffer object.")
        }
        mFBOHandle = mFrameBuf[0]

        // Generate Texture2D for FrameBuffer's Color Attachment
        // 把纹理的维度设置为屏幕大小：传入width和height，只分配相应的内存，而不填充
        mColorAttachmentTexture2D = XGLTexture2D(this, mWidth, mHeight)
        // 注册到TextureSlots, 以便ActiveTexture
        mColorAttachmentTextureIndex = XGLTextureSlots.getId(mColorAttachmentTexture2D)

        mQuad = FrameBufferQuad(mColorAttachmentTextureIndex)

        // Generate RBO for FrameBuffer's Stencil and Depth Attachment
        mRenderBuf = XGLRenderBuffer(mWidth, mHeight)

        // Bind FrameBuffer
        glBindFramebuffer(GL_FRAMEBUFFER, mFBOHandle)
        // Attach the Texture2D to the FrameBuffer as color buffer
        glFramebufferTexture2D(
            GL_FRAMEBUFFER,
            GL_COLOR_ATTACHMENT0,
            GL_TEXTURE_2D,
            mColorAttachmentTexture2D.mHandle,
            0
        )
        // Attach the Renderbuffer to FrameBuffer as Stencil buffer and Depth buffer
        glFramebufferRenderbuffer(
            GL_FRAMEBUFFER,
            GL_DEPTH_STENCIL_ATTACHMENT,
            GL_RENDERBUFFER,
            mRenderBuf.mHandle
        )

        val state: Boolean = (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE)
        check(state) { LOG.warn("Framebuffer is incomplete!") }

        // Switch back to the default framebuffer.
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        XGLContext.checkGLError("FrameBuffer init finish")
    }

    override fun close() {
        glDeleteFramebuffers(1, mFrameBuf)
        mRenderBuf.close()
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
        LOG.info("XGLFrameBuffer resizeColorAttachment($width, $height)")
        mWidth = width
        mHeight = height

        if (mFBOHandle != -1) {
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
            glBindTexture(GL_TEXTURE_2D, 0)

            glBindRenderbuffer(GL_RENDERBUFFER, mRenderBuf.mHandle)
            // 采用GL_DEPTH24_STENCIL8作为内部格式，它同时代表24位的深度和8位的模板缓冲
            glRenderbufferStorage(
                GL_RENDERBUFFER,
                GL_DEPTH24_STENCIL8,
                width,
                height
            )
            glBindRenderbuffer(GL_RENDERBUFFER, 0)
        }
    }

    fun drawOnScreen() {
        glClear(GL_COLOR_BUFFER_BIT)
        glDisable(GL_DEPTH_TEST)
        glDisable(GL_BLEND)

        mColorAttachmentTexture2D.bind(mColorAttachmentTextureIndex)
        mQuad.draw()

        // TODO: How to Swap the buffers?
        glViewport(0,0, mWidth, mHeight)

        glEnable(GL_DEPTH_TEST)
        glEnable(GL_BLEND)
    }
}