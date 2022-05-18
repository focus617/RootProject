package com.focus617.app_demo.renderer.framebuffer

import android.opengl.GLES31.*
import com.focus617.app_demo.engine.XGLContext
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

    private var mColorAttachments = mutableListOf<XGLTexture2DBuffer>()   // ColorTextureBuffers
    private var mDepthBufferAttachment: XGLTexture2DBuffer? = null        // DepthBuffer
    private var mRenderBufferAttachment: XGLRenderBuffer? = null          // RenderBuffer
    private val mQuad: FrameBufferEntity = FrameBufferEntity()

    private var mActiveColorAttachment: Int = 0

    // Must call under EGL
    init {
        for (format in specification.attachment.attachments) {
            if (!FrameBufferTextureFormat.isDepthFormat(format.textureFormat))
                mColorAttachmentSpecifications.add(format)
            else
                mDepthAttachmentSpecification = format
        }

        invalidate()

        mQuad.initOpenGlResource()
        LOG.info("XGLFrameBuffer created.")
    }


    private fun invalidate() {
        if (mHandle != -1) this.close()

        XGLContext.checkGLError("FrameBuffer init begin")
        val multiSample: Boolean = mSpecification.samples > 1

        initAttachments()
        XGLContext.checkGLError("FrameBufferAttachment initialized")

        // Generate FBO
        glGenFramebuffers(1, mFrameBuf)
        if (mFrameBuf[0] == 0) {
            throw RuntimeException("Could not create a new frame buffer object.")
        }
        mHandle = mFrameBuf[0]

        // Make FrameBuffer Active
        glBindFramebuffer(GL_FRAMEBUFFER, mHandle)
        // Attach the first Texture2D to FBO color attachment point
        glFramebufferTexture2D(
            GL_DRAW_FRAMEBUFFER,
            GL_COLOR_ATTACHMENT0,
            GL_TEXTURE_2D,
            mColorAttachments[mActiveColorAttachment].mHandle,
            0
        )

        // Attach the RenderBuffer to FBO Stencil and Depth attachment point
        mRenderBufferAttachment?.apply {
            glFramebufferRenderbuffer(
                GL_FRAMEBUFFER,
                GL_DEPTH_STENCIL_ATTACHMENT,
                GL_RENDERBUFFER,
                mRenderBufferAttachment!!.mHandle
            )
        }
        // Attach the DepthBuffer to FBO Depth attachment point
        mDepthBufferAttachment?.apply {
            glFramebufferRenderbuffer(
                GL_FRAMEBUFFER,
                GL_DEPTH_ATTACHMENT,
                GL_TEXTURE_2D,
                mDepthBufferAttachment!!.mHandle
            )
        }

        // check FBO status
        val state: Boolean = (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE)
        check(state) { LOG.warn("Framebuffer is incomplete!") }

        // Restore default framebuffer.
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        XGLContext.checkGLError("FrameBuffer init finish")
    }

    private fun initAttachments() {
        // Attachment
        if (mColorAttachmentSpecifications.size != 0) {
            for (spec in mColorAttachmentSpecifications) {
                when (spec.textureFormat) {
                    FrameBufferTextureFormat.RGBA8 -> {
                        // Generate Texture2D for FrameBuffer's Color Attachment
                        // 把纹理的维度设置为屏幕大小：传入width和height，只分配相应的内存，而不填充
                        val colorTextureBuf = XGLTexture2DBuffer(
                            FrameBufferTextureFormat.RGBA8,
                            mSpecification.mWidth,
                            mSpecification.mHeight
                        )
                        mColorAttachments.add(colorTextureBuf)
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
                    mRenderBufferAttachment = XGLRenderBuffer(
                        mSpecification.mWidth,
                        mSpecification.mHeight,
                        GL_DEPTH24_STENCIL8
                    )
                }
                FrameBufferTextureFormat.DEPTH_COMPONENT32F -> {
                    // Generate Texture2D for FrameBuffer's Depth Attachment
                    mDepthBufferAttachment = XGLTexture2DBuffer(
                        FrameBufferTextureFormat.DEPTH_COMPONENT32F,
                        mSpecification.mWidth,
                        mSpecification.mHeight
                    )
                }
                else -> {
                }
            }
        }
    }

    override fun close() {
        glDeleteFramebuffers(1, mFrameBuf)
        mDepthBufferAttachment?.close()
        mRenderBufferAttachment?.close()
        mColorAttachments.forEach { it.close() }
    }

    override fun bind() {
        /**
         * Bind a framebuffer object to the GL_DRAW_FRAMEBUFFER framebuffer binding point,
         * so that everything I render will end up in the FBO's attachments.
         *  */
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, mHandle)
        glViewport(0, 0, mSpecification.mWidth, mSpecification.mHeight)

        // glStencilMask(0x00)不仅会阻止模板缓冲的写入，也会阻止其清空(glClear(stencil_buffer)无效)
        glStencilMask(0xFF)

        // Clear the FrameBuffer's content.
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)
        glClearDepthf(1.0f)     // Setup the Depth buff
        glClearStencil(0)           // Setup the Stencil buff
    }

    override fun unbind() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0)
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

        mColorAttachments.forEach {
            if (it.screenTextureIndex != -1) {
                it.bind(it.screenTextureIndex)
                mQuad.draw(it.screenTextureIndex)
            }
        }

        glViewport(0, 0, mSpecification.mWidth, mSpecification.mHeight)

        glEnable(GL_DEPTH_TEST)
        glEnable(GL_BLEND)
    }

    companion object {
        const val sMaxFrameBufferSize = 8192
    }
}