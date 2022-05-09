package com.focus617.app_demo.renderer

import android.opengl.GLES20.GL_FRAMEBUFFER
import android.opengl.GLES31
import com.focus617.core.engine.renderer.Framebuffer
import com.focus617.platform.helper.TextureHelper
import java.nio.IntBuffer


class XGLFrameBuffer(width: Int, height: Int) : Framebuffer(width, height) {
    private var mFrameBuf: IntBuffer = IntBuffer.allocate(1)
    private var mHandle: Int = 0

    private var mTextureBuf: IntArray = IntArray(1)
    private var mColorAttachmentTextureId: Int = 0

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
        GLES31.glGenTextures(1, mTextureBuf, 0)
        if (mTextureBuf[0] == 0) {
            LOG.error("Could not generate a new OpenGL texture object.")
        }
        mColorAttachmentTextureId = mTextureBuf[0]
        // Bind to the texture in OpenGL
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, mColorAttachmentTextureId)
        //绑定纹理单元与sampler
        GLES31.glBindSampler(mColorAttachmentTextureId, TextureHelper.samplers[0])
        // Allocate texture storage
        GLES31.glTexImage2D(
            GLES31.GL_TEXTURE_2D,
            0,
            GLES31.GL_RGBA,
            width,
            height,
            0,
            GLES31.GL_RGBA,
            GLES31.GL_UNSIGNED_BYTE,
            null
        )

        // attach it to the frame buffer, 作为输出的texture
        GLES31.glFramebufferTexture2D(
            GLES31.GL_FRAMEBUFFER,
            GLES31.GL_COLOR_ATTACHMENT0,
            GLES31.GL_TEXTURE_2D,
            mColorAttachmentTextureId,
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
        GLES31.glDeleteTextures(1, mTextureBuf, 0)
    }

    fun getColorAttachmentTextureId() = mColorAttachmentTextureId

    override fun getColorAttachmentTexture2DId() = mColorAttachmentTextureId

    override fun bind() {
        GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, mHandle)
    }

    override fun unbind() {
        GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, 0)
    }
}