package com.focus617.app_demo.renderer

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20.*
import android.opengl.GLES30
import android.opengl.GLES30.GL_RGBA8
import android.opengl.GLES31
import com.focus617.core.engine.renderer.Texture2D
import com.focus617.platform.helper.BitmapHelper
import java.nio.Buffer
import java.nio.ByteBuffer

/**
 * OpenGL纹理类 XGLTexture
 * 1. 储存了纹理的基本属性 [mWidth] [mHeight]
 * 2. 它的构造器需要纹理的图片资源或文件
 */
class XGLTexture2D private constructor(filePath: String) : Texture2D(filePath) {
    private val textureObjectIdBuf = IntArray(1)
    var textureObjectId: Int = 0

    private var mInternalFormat: Int = GL_RGBA8
    private var mDataFormat: Int = GL_RGBA

    override var mWidth: Int = 0
    override var mHeight: Int = 0

    override fun equals(other: Any?): Boolean =
        if (other !is XGLTexture2D) false
        else textureObjectId == other.textureObjectId

    /** 基于Assets中的文件构造 */
    constructor(context: Context, filePath: String) : this(filePath) {
        val bitmap = BitmapHelper.bitmapLoader(context, filePath)
        initTexture(bitmap)
    }

    /** 基于Resource/raw中的文件构造 */
    constructor(context: Context, resourceId: Int) : this("Resource/$resourceId") {
        val bitmap = BitmapHelper.bitmapLoader(context, resourceId)
        initTexture(bitmap)
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
        textureObjectId = textureObjectIdBuf[0]

        // Bind to the texture in OpenGL
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureObjectId)

        // Allocate texture storage
        GLES31.glTexStorage2D(GL_TEXTURE_2D, 1, mInternalFormat, mWidth, mHeight)

        glTexParameteri(textureObjectId, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(textureObjectId, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        glTexParameteri(textureObjectId, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(textureObjectId, GL_TEXTURE_WRAP_T, GL_REPEAT)
    }

    private fun initTexture(bitmap: Bitmap) {
        bitmap.apply {
            mWidth = bitmap.width
            mHeight = bitmap.height
            loadImageIntoTexture(bitmap)
        }
        // Recycle the bitmap, since its data has been loaded into OpenGL.
        bitmap.recycle()
    }

    override fun setData(data: Buffer, size: Int) {
        val bpp = if (mDataFormat == GL_RGBA) 4 else 3
        require(size == (mWidth * mHeight * bpp)) { "Data must be entire texture!" }

        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, textureObjectId)
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
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureObjectId)
    }

    override fun close() {
        GLES31.glDeleteTextures(1, textureObjectIdBuf, 0)
    }

    /**
     * Loads a texture from a file, returning the OpenGL ID for that
     * texture. Returns 0 if the load failed.
     *
     * @param bitmap
     * @return textureObjectId
     */
    private fun loadImageIntoTexture(bitmap: Bitmap): Int {

        GLES31.glGenTextures(1, textureObjectIdBuf, 0)
        if (textureObjectIdBuf[0] == 0) {
            LOG.error("Could not generate a new OpenGL texture object.")
            return 0
        }
        textureObjectId = textureObjectIdBuf[0]

        // Bind to the texture in OpenGL
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureObjectId)

        // Set
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)

        // Set filtering: a default must be set, or the texture will be black.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        val byteBuf = ByteBuffer.allocate(bitmap.width * bitmap.height * 4)
        bitmap.copyPixelsToBuffer(byteBuf)
        byteBuf.position(0)

        // Load the bitmap into the bound texture.
        GLES31.glTexImage2D(
            GLES30.GL_TEXTURE_2D,
            0,
            GLES30.GL_RGBA,
            bitmap.width,
            bitmap.height,
            0,
            GLES30.GL_RGBA,
            GLES30.GL_UNSIGNED_BYTE,
            byteBuf
        )
        //  GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, bitmap, 0)

        // Note: Following code may cause an error to be reported in the
        // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
        // Failed to generate texture mipmap levels (error=3)
        // No OpenGL error will be encountered (glGetError() will return
        // 0). If this happens, just squash the source image to be
        // square. It will look the same because of texture coordinates,
        // and mipmap generation will work.
        GLES31.glGenerateMipmap(GLES31.GL_TEXTURE_2D)

        // Unbind from the texture.
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, 0)

        return textureObjectId
    }

}