package com.focus617.app_demo.renderer.texture

import android.content.Context
import android.opengl.GLES31.*
import com.focus617.core.engine.renderer.Framebuffer
import com.focus617.core.engine.renderer.Texture2D
import com.focus617.platform.helper.BitmapHelper
import com.focus617.platform.helper.TextureHelper
import java.nio.Buffer

/**
 * OpenGL纹理类 XGLTexture
 * 1. 储存了纹理的基本属性 [mWidth] [mHeight]
 * 2. 它的构造器需要纹理的图片资源或文件
 */
class XGLTexture2D private constructor(filePath: String) : Texture2D(filePath) {
    private val mHandleBuf = IntArray(1)
    override var mHandle: Int = 0

    private var mInternalFormat: Int = GL_RGBA8
    private var mDataFormat: Int = GL_RGBA

    override var mWidth: Int = 0
    override var mHeight: Int = 0

    override fun equals(other: Any?): Boolean =
        if (other !is XGLTexture2D) false
        else mHandle == other.mHandle

    /** 基于Assets中的文件构造 */
    constructor(context: Context, filePath: String) : this(filePath) {
        val bitmap = BitmapHelper.bitmapLoader(context, filePath)
        bitmap.apply {
            mWidth = bitmap.width
            mHeight = bitmap.height
            mHandle = TextureHelper.loadImageIntoTexture(mHandleBuf, bitmap)
        }
        // Recycle the bitmap, since its data has been loaded into OpenGL.
        bitmap.recycle()
    }

    /** 基于Resource/raw中的文件构造 */
    constructor(context: Context, resourceId: Int) : this("Resource/$resourceId") {
        val bitmap = BitmapHelper.bitmapLoader(context, resourceId)
        bitmap.apply {
            mWidth = bitmap.width
            mHeight = bitmap.height
            mHandle = TextureHelper.loadImageIntoTexture(mHandleBuf, bitmap)
        }
        // Recycle the bitmap, since its data has been loaded into OpenGL.
        bitmap.recycle()
    }

    /** 程序编程构造 */
    constructor(width: Int, height: Int) : this("Program/generated") {
        mWidth = width
        mHeight = height

        mInternalFormat = GL_RGBA8
        mDataFormat = GL_RGBA

        glGenTextures(1, mHandleBuf, 0)
        if (mHandleBuf[0] == 0) {
            LOG.error("Could not generate a new OpenGL texture object.")
        }
        mHandle = mHandleBuf[0]

        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, mHandle)

        // Allocate texture storage
        glTexStorage2D(GL_TEXTURE_2D, 1, mInternalFormat, mWidth, mHeight)

        //绑定纹理单元与sampler
        glBindSampler(mHandle, TextureHelper.samplers[0])

        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    // 创建Texture2D，作为FrameBuffer的output image
    constructor(framebuffer: Framebuffer, width: Int, height: Int) : this("FrameBuffer") {
        mWidth = width
        mHeight = height

        glGenTextures(1, mHandleBuf, 0)
        if (mHandleBuf[0] == 0) {
            LOG.error("Could not generate a new OpenGL texture object.")
        }
        mHandle = mHandleBuf[0]
        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, mHandle)
//        glEnable(GL_MULTISAMPLE)

        //绑定纹理单元与sampler
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)

        // Allocate texture storage
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
        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    override fun setData(data: Buffer, size: Int) {
        val bpp = if (mDataFormat == GL_RGBA) 4 else 3
        require(size == (mWidth * mHeight * bpp)) { "Data must be entire texture!" }

        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, mHandle)
        glTexSubImage2D(
            GL_TEXTURE_2D,
            0,
            0,
            0,
            mWidth,
            mHeight,
            mDataFormat,
            GL_UNSIGNED_BYTE,
            data
        )
        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0)
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

    override fun close() {
        glDeleteTextures(1, mHandleBuf, 0)
    }

}