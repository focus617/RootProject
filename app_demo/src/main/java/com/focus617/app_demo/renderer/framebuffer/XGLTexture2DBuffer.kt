package com.focus617.app_demo.renderer.framebuffer

import android.graphics.Bitmap
import android.opengl.GLES31.*
import com.focus617.app_demo.renderer.texture.XGLTextureSlots
import com.focus617.core.engine.renderer.framebuffer.FrameBufferTextureFormat
import com.focus617.core.engine.renderer.framebuffer.IfFrameBufferAttachment
import com.focus617.core.engine.renderer.texture.Texture2D
import java.nio.Buffer
import java.nio.ByteBuffer

class XGLTexture2DBuffer private constructor() :
    Texture2D("FrameBufferAttachment"), IfFrameBufferAttachment {
    private val mHandleBuf = IntArray(1)
    override var mHandle: Int = -1
    var screenTextureIndex: Int = -1    // 在TextureSlots内的Index

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
            FrameBufferTextureFormat.RGBA8 -> {
                createColorStorage(width, height)
                // 注册到TextureSlots, 获得TextureUnit Index, 以便ActiveTexture
                screenTextureIndex = XGLTextureSlots.requestIndex(this)
            }

            // 作为FrameBuffer的Color Attachment
            FrameBufferTextureFormat.DEPTH_COMPONENT32F -> createDepthStorage(width, height)

            else -> {
                LOG.error("Unknown Texture Format!")
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

    // Used for render this texture to screen
    override fun bind(slot: Int) {
        if(screenTextureIndex != -1) {
            // Set active texture unit
            glActiveTexture(XGLTextureSlots.getTextureUnit(screenTextureIndex))
            // Bind this texture with above active texture unit
            glBindTexture(GL_TEXTURE_2D, mHandle)
        }
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

    fun setData(bitmap: Bitmap, xOffset: Int, yOffset: Int) {
        require((xOffset + bitmap.width) < mWidth) {
            "Bitmap width(${bitmap.width} plug xoffset($xOffset) must less than texture width($mWidth)"
        }
        require((yOffset + bitmap.height) < mHeight) {
            "Bitmap width(${bitmap.height} plug xoffset($yOffset) must less than texture height($mHeight)"
        }

        val size = bitmap.width * bitmap.height * 4
        val byteBuf = ByteBuffer.allocate(size)
        bitmap.copyPixelsToBuffer(byteBuf)
        byteBuf.position(0)

        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, mHandle)
        glTexSubImage2D(
            GL_TEXTURE_2D,
            0,
            xOffset,
            yOffset,
            bitmap.width,
            bitmap.height,
            GL_RGBA,
            GL_UNSIGNED_BYTE,
            byteBuf
        )
        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0)
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