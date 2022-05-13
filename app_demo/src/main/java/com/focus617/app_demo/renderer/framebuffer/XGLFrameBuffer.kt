package com.focus617.app_demo.renderer.framebuffer

import android.opengl.GLES31.*
import com.focus617.app_demo.engine.XGLContext
import com.focus617.app_demo.renderer.texture.XGLTexture2D
import com.focus617.app_demo.renderer.texture.XGLTextureSlots
import com.focus617.core.engine.renderer.framebuffer.FrameBuffer
import com.focus617.core.engine.renderer.framebuffer.FrameBufferSpecification
import com.focus617.core.engine.renderer.framebuffer.FrameBufferTextureFormat
import com.focus617.core.engine.renderer.framebuffer.FrameBufferTextureSpecification
import java.nio.IntBuffer


class XGLFrameBuffer(specification: FrameBufferSpecification) : FrameBuffer(specification) {

    private var mFrameBuf: IntBuffer = IntBuffer.allocate(1)
    private var mHandle: Int = -1   // FrameBuffer的Handle

    private val mColorAttachmentSpecifications = mutableListOf<FrameBufferTextureSpecification>()
    private var mDepthAttachmentSpecification =
        FrameBufferTextureSpecification(FrameBufferTextureFormat.None)

    private var mColorAttachments = mutableListOf<XGLTexture2D>()  // ColorTextureBuffers
    private lateinit var mRenderBufferAttachment: XGLRenderBuffer // RenderBuffer
    private lateinit var mQuad: FrameBufferQuad

    init {
        LOG.info("XGLFrameBuffer created.")

        for (format in specification.attachment.attachments) {
            if (!isDepthFormat(format.textureFormat))
                mColorAttachmentSpecifications.add(format)
            else
                mDepthAttachmentSpecification = format
        }

        invalidate()
    }


    private fun invalidate() {
        if (mHandle != -1) this.close()

        val multiSample: Boolean = mSpecification.samples > 1
        // Attachment
        if (mColorAttachmentSpecifications.size != 0) {
            for (spec in mColorAttachmentSpecifications) {
                when (spec.textureFormat) {
                    FrameBufferTextureFormat.RGBA8 -> {
                        // Generate Texture2D for FrameBuffer's Color Attachment
                        // 把纹理的维度设置为屏幕大小：传入width和height，只分配相应的内存，而不填充
                        val colorTextureBuf =
                            XGLTexture2D(this, mSpecification.mWidth, mSpecification.mHeight)
                        mColorAttachments.add(colorTextureBuf)

                        // 注册到TextureSlots, 以便ActiveTexture
                        FrameBufferQuad.screenTextureIndex = XGLTextureSlots.getId(colorTextureBuf)

                        mQuad = FrameBufferQuad()

                    }
                    else -> {
                    }
                }
            }
        }

        if (mDepthAttachmentSpecification.textureFormat != FrameBufferTextureFormat.None) {
            when (mDepthAttachmentSpecification.textureFormat) {
                FrameBufferTextureFormat.DEPTH24STENCIL8 -> {
                    // Generate RBO for FrameBuffer's Stencil and Depth Attachment
                    mRenderBufferAttachment =
                        XGLRenderBuffer(mSpecification.mWidth, mSpecification.mHeight, GL_DEPTH24_STENCIL8)
                }
                else -> {
                }
            }
        }

        // Generate FBO
        glGenFramebuffers(1, mFrameBuf)
        if (mFrameBuf[0] == 0) {
            throw RuntimeException("Could not create a new frame buffer object.")
        }
        mHandle = mFrameBuf[0]
        // Bind FrameBuffer
        glBindFramebuffer(GL_FRAMEBUFFER, mHandle)
        // Attach the Texture2D to FBO color attachment point
        glFramebufferTexture2D(
            GL_FRAMEBUFFER,
            GL_COLOR_ATTACHMENT0,
            GL_TEXTURE_2D,
            mColorAttachments[0].mHandle,
            0
        )
        // Attach the Renderbuffer to FBO Stencil and Depth attachment point
        glFramebufferRenderbuffer(
            GL_FRAMEBUFFER,
            GL_DEPTH_STENCIL_ATTACHMENT,
            GL_RENDERBUFFER,
            mRenderBufferAttachment.mHandle
        )
        // check FBO status
        val state: Boolean = (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE)
        check(state) { LOG.warn("Framebuffer is incomplete!") }

        // Switch back to the default framebuffer.
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        XGLContext.checkGLError("FrameBuffer init finish")
    }

    override fun close() {
        glDeleteFramebuffers(1, mFrameBuf)
        mRenderBufferAttachment.close()
        mColorAttachments.forEach() { it.close() }
    }

    override fun bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, mHandle)
        glViewport(0, 0, mSpecification.mWidth, mSpecification.mHeight)
    }

    override fun unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    override fun getColorAttachmentTextureId(index: Int) = mColorAttachments[index].mHandle

    override fun resizeColorAttachment(width: Int, height: Int) {
        LOG.info("XGLFrameBuffer resizeColorAttachment($width, $height)")
        if (width == 0 || height == 0 || width > sMaxFrameBufferSize || height > sMaxFrameBufferSize) {
            LOG.warn("Attempt to resize framebuffer to $width, $height")
            return
        }
        mSpecification.mWidth = width
        mSpecification.mHeight = height
        invalidate()
    }

    fun drawOnScreen() {
        glClear(GL_COLOR_BUFFER_BIT)
        glDisable(GL_DEPTH_TEST)
        glDisable(GL_BLEND)

        mColorAttachments[0].bind(FrameBufferQuad.screenTextureIndex)
        mQuad.draw()

        // TODO: How to Swap the buffers?
        glViewport(0, 0, mSpecification.mWidth, mSpecification.mHeight)

        glEnable(GL_DEPTH_TEST)
        glEnable(GL_BLEND)
    }

    companion object {
        const val sMaxFrameBufferSize = 8192

        fun isDepthFormat(format: FrameBufferTextureFormat): Boolean =
            when (format) {
                FrameBufferTextureFormat.DEPTH24STENCIL8 -> true
                else -> false
            }


    }
}