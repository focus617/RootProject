package com.focus617.opengles.renderer.framebuffer

import android.opengl.GLES31.*
import com.focus617.core.engine.renderer.framebuffer.FrameBuffer
import com.focus617.core.engine.renderer.framebuffer.FrameBufferSpecification
import com.focus617.core.engine.renderer.framebuffer.FrameBufferTextureFormat
import com.focus617.core.engine.renderer.framebuffer.FrameBufferTextureSpecification
import com.focus617.opengles.egl.XGLContext
import timber.log.Timber
import java.nio.IntBuffer


class XGLFrameBuffer(specification: FrameBufferSpecification) : FrameBuffer(specification) {

    private var mFrameBuf: IntBuffer = IntBuffer.allocate(1)
    private var mHandle: Int = -1   // FrameBuffer的Handle

    private val mColorAttachmentSpecifications = mutableListOf<FrameBufferTextureSpecification>()
    private var mDepthAttachmentSpecification =
        FrameBufferTextureSpecification(FrameBufferTextureFormat.None)

    private val isMultiSample = mSpecification.samples > 1
    private var mMaxColorAttachment = 0
    private var mColorAttachments = mutableListOf<XGLTexture2DBuffer>()   // ColorTextureBuffers
    private var mMultiSampleRenderBuffersForColor = mutableListOf<XGLRenderBuffer>()

    private var mDepthBufferAttachment: XGLTexture2DBuffer? = null        // DepthBuffer
    private var mRenderBufferAttachment: XGLRenderBuffer? = null          // RenderBuffer
    private var mMultiSampleRenderBufferForDepth: XGLRenderBuffer? = null // MultiSampleRenderBuffer

    private val mQuad: FrameBufferEntity = FrameBufferEntity()

    // Must call under EGL
    init {
        mMaxColorAttachment = getMaxColorAttachmentNumber()

        for (format in specification.attachment.attachments) {
            if (!FrameBufferTextureFormat.isDepthFormat(format.textureFormat))
                mColorAttachmentSpecifications.add(format)
            else
                mDepthAttachmentSpecification = format
        }

        invalidate()

        mQuad.initOpenGlResource()

        if (isMultiSample)
            Timber.i("Created XGLFrameBuffer with multiSample RenderBuffers")
        else
            Timber.i(
                "Created XGLFrameBuffer with ${mColorAttachments.size} ColorAttachment" +
                        "and ${if (mRenderBufferAttachment != null) "one" else "null"} depthAttachment."
            )
    }

    // Must be called in opengl env, such as scene.initOpenGlResource()
    private fun getMaxColorAttachmentNumber(): Int {
        val numberBuf = IntBuffer.allocate(1)
        glGetIntegerv(GL_MAX_COLOR_ATTACHMENTS, numberBuf)
        return numberBuf[0]
    }

    override fun close() {
        mColorAttachments.forEach { it.close() }
        mMultiSampleRenderBuffersForColor.forEach { it.close() }

        mDepthBufferAttachment?.close()
        mRenderBufferAttachment?.close()
        mMultiSampleRenderBufferForDepth?.close()

        glDeleteFramebuffers(1, mFrameBuf)

        mDepthBufferAttachment = null
        mRenderBufferAttachment = null
        mMultiSampleRenderBufferForDepth = null
        mHandle = -1
    }

    private fun invalidate() {
        if (mHandle != -1) this.close()

        XGLContext.checkGLError("FrameBuffer init begin")

        initColorAttachments()
        initDepthAttachments()
        XGLContext.checkGLError("FrameBufferAttachment initialized")

        // Generate FBO
        glGenFramebuffers(1, mFrameBuf)
        if (mFrameBuf[0] == 0) {
            throw RuntimeException("Could not create a new frame buffer object.")
        }
        mHandle = mFrameBuf[0]

        // Make FrameBuffer Active
        glBindFramebuffer(GL_FRAMEBUFFER, mHandle)

        if (isMultiSample)
            for ((index, renderBuffer) in mMultiSampleRenderBuffersForColor.withIndex()) {
                // Attach multiSampler RenderBuffer
                glFramebufferRenderbuffer(
                    GL_FRAMEBUFFER,
                    GL_COLOR_ATTACHMENT0 + index,
                    GL_RENDERBUFFER,
                    renderBuffer.mHandle
                )
                XGLContext.checkGLError("glFramebufferRenderbuffer")
            }
        else
            for ((index, colorAttachment) in mColorAttachments.withIndex()) {

                // Attach the Texture2D as color attachment to FBO color attachment point
                glFramebufferTexture2D(
                    GL_DRAW_FRAMEBUFFER,
                    GL_COLOR_ATTACHMENT0 + index,
                    GL_TEXTURE_2D,
                    colorAttachment.mHandle,
                    0
                )
            }


        if (isMultiSample) {
            mMultiSampleRenderBufferForDepth?.apply {
                glFramebufferRenderbuffer(
                    GL_FRAMEBUFFER,
                    GL_DEPTH_ATTACHMENT,
                    GL_RENDERBUFFER,
                    mMultiSampleRenderBufferForDepth!!.mHandle
                )
            }

        } else {
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
        }

        // check FBO status
        val state = glCheckFramebufferStatus(GL_FRAMEBUFFER)
        check(state == GL_FRAMEBUFFER_COMPLETE)
        {
            Timber.w("Framebuffer is incomplete! State=$state")
        }

        // Restore default framebuffer.
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        XGLContext.checkGLError("FrameBuffer init finish")
    }

    private fun initColorAttachments() {
        // Attachment
        for (spec in mColorAttachmentSpecifications) {
            if (mColorAttachments.size < mMaxColorAttachment) {
                //TODO: only this format?
                if (spec.textureFormat == FrameBufferTextureFormat.RGBA8) {
                    // opengl es only support renderbuffer for multisampling.
                    if (isMultiSample) {
                        // Generate multiSample RenderBuffer
                        val renderBuffer = XGLRenderBuffer(
                            mSpecification.mWidth,
                            mSpecification.mHeight,
                            mSpecification.samples,
                            GL_RGBA8
                        )
                        mMultiSampleRenderBuffersForColor.add(renderBuffer)

                    } else {
                        // Generate Texture2D for FrameBuffer's Color Attachment
                        // 把纹理的维度设置为屏幕大小：传入width和height，只分配相应的内存，而不填充
                        val colorTextureBuf = XGLTexture2DBuffer(
                            FrameBufferTextureFormat.RGBA8,
                            mSpecification.mWidth,
                            mSpecification.mHeight
                        )
                        mColorAttachments.add(colorTextureBuf)
                    }

                } else {
                    Timber.w("Unknown color attachment format!")
                    continue
                }
            } else break
        }
    }

    private fun initDepthAttachments() {
        // opengl es only support renderbuffer for multisampling.
        if (isMultiSample) {
            // Generate multiSample RenderBuffer
            mMultiSampleRenderBufferForDepth = XGLRenderBuffer(
                mSpecification.mWidth,
                mSpecification.mHeight,
                mSpecification.samples,
                GL_DEPTH_COMPONENT16    //TODO: only this format?
            )

        } else {

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


    override fun bind() {
        /**
         * Bind a framebuffer object to the GL_DRAW_FRAMEBUFFER framebuffer binding point,
         * so that everything I render will end up in the FBO's attachments.
         *  */
        if (isMultiSample) {
            glBindFramebuffer(GL_DRAW_FRAMEBUFFER, mHandle)
            mMultiSampleRenderBuffersForColor.forEach { it.bind() }
        } else {
            glBindFramebuffer(GL_DRAW_FRAMEBUFFER, mHandle)
            glViewport(0, 0, mSpecification.mWidth, mSpecification.mHeight)
        }
        // glStencilMask(0x00)不仅会阻止模板缓冲的写入，也会阻止其清空(glClear(stencil_buffer)无效)
        glStencilMask(0xFF)

        // Clear the FrameBuffer's content.
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)
        glClearDepthf(1.0f)     // Setup the Depth buff
        glClearStencil(0)           // Setup the Stencil buff

    }

    override fun unbind() {
        if (isMultiSample) {
            glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0)
            mMultiSampleRenderBuffersForColor.forEach { it.unbind() }
        } else {
            glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0)
        }
    }

    override fun getColorAttachmentTextureId(index: Int): Int {
        check(index < mColorAttachments.size) {
            "Index is larger than real size of ColorAttachments"
        }
        return mColorAttachments[index].mHandle
    }

    override fun resize(width: Int, height: Int) {
        Timber.i("XGLFrameBuffer resize to ($width, $height)")
        if (width == 0 || height == 0 || width > sMaxFrameBufferSize || height > sMaxFrameBufferSize) {
            Timber.w("Attempt to resize framebuffer to $width, $height")
            return
        }
        mSpecification.mWidth = width
        mSpecification.mHeight = height
        invalidate()
    }

    override fun readPixel(attachmentIndex: Int, x: Int, y: Int): Int {
        check(attachmentIndex < mColorAttachments.size) {
            "Index is larger than real size of ColorAttachments"
        }
        glReadBuffer(GL_COLOR_ATTACHMENT0 + attachmentIndex)
        val pixelData = IntBuffer.allocate(1)
        glReadPixels(x, y, 1, 1, GL_RED_INTEGER, GL_INT, pixelData)
        return pixelData[0]
    }

    fun drawOnScreen() {
        glClear(GL_COLOR_BUFFER_BIT)
        glDisable(GL_DEPTH_TEST)
        glDisable(GL_BLEND)

        if (isMultiSample) {
            // now blit multiSampled buffer(s) to normal ColorBuffer of intermediate FBO.
            // Image is stored in screenTexture
            glBindFramebuffer(GL_READ_FRAMEBUFFER, mHandle)
            glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0)
            mMultiSampleRenderBuffersForColor.forEach {
                glBlitFramebuffer(
                    0,
                    0,
                    mSpecification.mWidth,
                    mSpecification.mHeight,
                    0,
                    0,
                    mSpecification.mWidth,
                    mSpecification.mHeight,
                    GL_COLOR_BUFFER_BIT,
                    GL_NEAREST
                )
                glBlitFramebuffer(
                    0,
                    0,
                    mSpecification.mWidth,
                    mSpecification.mHeight,
                    0,
                    0,
                    mSpecification.mWidth,
                    mSpecification.mHeight,
                    GL_DEPTH_BUFFER_BIT,
                    GL_NEAREST
                )
            }
        } else {
            mColorAttachments.forEach {
                if (it.screenTextureIndex != -1) {
                    it.bind(it.screenTextureIndex)
                    mQuad.draw(it.screenTextureIndex)
                }
            }
        }

        glViewport(0, 0, mSpecification.mWidth, mSpecification.mHeight)
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_BLEND)
    }

    companion object {
        const val sMaxFrameBufferSize = 8192

        fun FrameBufferTextureFormatToGlValue(format: FrameBufferTextureFormat): Int =
            when (format) {
                FrameBufferTextureFormat.RGBA8 -> GL_RGBA8
                FrameBufferTextureFormat.RED_INTEGER -> GL_RED_INTEGER
                else -> 0
            }
    }
}