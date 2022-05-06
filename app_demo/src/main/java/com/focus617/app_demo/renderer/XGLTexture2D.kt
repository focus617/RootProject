package com.focus617.app_demo.renderer

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLES30.GL_RGBA8
import android.opengl.GLES31
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
    private val textureObjectIdBuf = IntArray(1)
    var mHandle: Int = 0

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
            mHandle = TextureHelper.loadImageIntoTexture(textureObjectIdBuf, bitmap)
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
            mHandle = TextureHelper.loadImageIntoTexture(textureObjectIdBuf, bitmap)
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

        GLES31.glGenTextures(1, textureObjectIdBuf, 0)
        if (textureObjectIdBuf[0] == 0) {
            LOG.error("Could not generate a new OpenGL texture object.")
        }
        mHandle = textureObjectIdBuf[0]

        // Bind to the texture in OpenGL
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, mHandle)

        // Allocate texture storage
        GLES31.glTexStorage2D(GL_TEXTURE_2D, 1, mInternalFormat, mWidth, mHeight)

        glTexParameteri(mHandle, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
        glTexParameteri(mHandle, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glTexParameteri(mHandle, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(mHandle, GL_TEXTURE_WRAP_T, GL_REPEAT)
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
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, 0)
    }

    override fun bind(slot: Int) {
        when (slot) {
            0 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE0)
            1 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE1)
            2 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE2)
            3 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE3)
            4 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE4)
            5 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE5)
            6 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE6)
            7 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE7)
            8 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE8)
            9 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE9)
            10 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE10)
            11 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE11)
            12 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE12)
            13 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE13)
            14 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE14)
            else -> GLES31.glActiveTexture(GLES31.GL_TEXTURE15)
        }

        // Bind this texture with above active texture
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, mHandle)
    }

    override fun close() {
        GLES31.glDeleteTextures(1, textureObjectIdBuf, 0)
    }

}