package com.focus617.app_demo.renderer.framebuffer

import android.opengl.GLES31.*
import com.focus617.core.engine.renderer.framebuffer.FrameBufferTextureFormat
import com.focus617.core.engine.renderer.framebuffer.IfFrameBufferAttachment
import com.focus617.core.engine.renderer.texture.Texture2D
import java.io.Closeable
import java.nio.Buffer

class XGLTexture2DBuffer private constructor() :
    Texture2D("FrameBufferAttachment"), Closeable, IfFrameBufferAttachment {
    private val mHandleBuf = IntArray(1)
    override var mHandle: Int = -1

    override var mWidth: Int = 0
    override var mHeight: Int = 0

    // 创建Texture2D，作为FrameBuffer的Attachment
    constructor(format: FrameBufferTextureFormat, width: Int, height: Int) : this() {
        mWidth = width
        mHeight = height

        glGenTextures(1, mHandleBuf, 0)
        if (mHandleBuf[0] == 0) {
            LOG.error("Could not generate a new OpenGL texture object.")
        }
        mHandle = mHandleBuf[0]
        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, mHandle)

        when (format) {
            // 作为FrameBuffer的Color Attachment
            FrameBufferTextureFormat.RGBA8 -> createColorStorage(width, height)
            // 作为FrameBuffer的Color Attachment
            FrameBufferTextureFormat.DEPTH_COMPONENT32F -> createDepthStorage(width, height)
            else -> {
                LOG.error("Wrong Texture Format!")
            }
        }
        //绑定纹理单元与sampler
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)

        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    override fun bind() {
        // Bind this texture with framebuffer
        glBindTexture(GL_TEXTURE_2D, mHandle)
    }

    override fun unbind() {
        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    override fun close() {
        glDeleteTextures(1, mHandleBuf, 0)
    }

    override fun setData(data: Buffer, size: Int) {
        TODO("Not yet implemented")
    }

    override fun bind(slot: Int) {
        val textureSlot = when (slot) {
            0 -> GL_TEXTURE0
            1 -> GL_TEXTURE1
            2 -> GL_TEXTURE2
            3 -> GL_TEXTURE3
            4 -> GL_TEXTURE4
            5 -> GL_TEXTURE5
            6 -> GL_TEXTURE6
            7 -> GL_TEXTURE7
            8 -> GL_TEXTURE8
            9 -> GL_TEXTURE9
            10 -> GL_TEXTURE10
            11 -> GL_TEXTURE11
            12 -> GL_TEXTURE12
            13 -> GL_TEXTURE13
            14 -> GL_TEXTURE14
            else -> GL_TEXTURE15
        }
        // Set active texture unit
        glActiveTexture(textureSlot)

        // Bind this texture with above active texture unit
        glBindTexture(GL_TEXTURE_2D, mHandle)
    }

    private fun createColorStorage(width: Int, height: Int) {
        // Allocate texture storage
        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            GL_RGBA8,
            width,
            height,
            0,
            GL_RGBA,
            GL_UNSIGNED_BYTE,
            null
        )
    }

    private fun createDepthStorage(width: Int, height: Int) {
        // Allocate texture storage
        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            GL_DEPTH_COMPONENT32F,
            width,
            height,
            0,
            GL_DEPTH_COMPONENT,
            GL_FLOAT,
            null
        )
    }


}