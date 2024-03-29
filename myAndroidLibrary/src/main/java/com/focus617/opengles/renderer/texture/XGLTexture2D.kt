package com.focus617.opengles.renderer.texture

import android.content.Context
import android.opengl.GLES31.*
import com.focus617.core.engine.renderer.texture.Texture2D
import com.focus617.opengles.egl.XGLContext
import com.focus617.platform.helper.BitmapHelper
import timber.log.Timber
import java.nio.Buffer


/**
 * OpenGL纹理类 XGLTexture
 * 1. 储存了纹理的基本属性 [mWidth] [mHeight]
 * 2. 它的构造器需要纹理的图片资源或文件
 */
open class XGLTexture2D(filePath: String) : Texture2D(filePath) {
    private val mHandleBuf = IntArray(1)
    final override var mHandle: Int = 0

    final override var mWidth: Int = 0
    final override var mHeight: Int = 0

    protected var mInternalFormat: Int = GL_RGBA8
    protected var mDataFormat: Int = GL_RGBA

    override fun equals(other: Any?): Boolean =
        if (other !is XGLTexture2D) false
        else mHandle == other.mHandle

    /** 最基础构造 */
    init{
        glGenTextures(1, mHandleBuf, 0)
        if (mHandleBuf[0] == 0) {
           Timber.e("Could not generate a new OpenGL texture object.")
        }
        mHandle = mHandleBuf[0]
    }

    override fun close() {
        glDeleteTextures(1, mHandleBuf, 0)
    }

    /** 基于Assets中的文件构造 */
    constructor(context: Context, filePath: String) : this(filePath) {
        val bitmap = BitmapHelper.bitmapLoader(context, filePath)
        bitmap.apply {
            mWidth = bitmap.width
            mHeight = bitmap.height
            XGLTextureHelper.loadImageIntoMutableTexture(mHandle, bitmap)

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle()

            //绑定纹理单元与sampler
            glBindSampler(mHandle, XGLTextureHelper.samplers[0])
            XGLContext.checkGLError("glBindSampler")
        }
    }

    /** 基于Resource/raw中的文件构造 */
    constructor(context: Context, resourceId: Int) : this("Resource/$resourceId") {
        val bitmap = BitmapHelper.bitmapLoader(context, resourceId)
        bitmap.apply {
            mWidth = bitmap.width
            mHeight = bitmap.height
            XGLTextureHelper.loadImageIntoMutableTexture(mHandle, bitmap)

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle()

            //绑定纹理单元与sampler
            glBindSampler(mHandle, XGLTextureHelper.samplers[0])
            XGLContext.checkGLError("glBindSampler")
        }
    }

    /** 程序编程构造 */
    constructor(width: Int, height: Int) : this("Program/generated") {
        mWidth = width
        mHeight = height

        mInternalFormat = GL_RGBA8
        mDataFormat = GL_RGBA

        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, mHandle)

        //绑定纹理单元与sampler
        glBindSampler(mHandle, XGLTextureHelper.samplers[0])

        // Allocate texture storage(fix format/buffer size)
        glTexStorage2D(GL_TEXTURE_2D, 1, mInternalFormat, mWidth, mHeight)

        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    // 将Text生成Bitmap，然后将之动态加载到Texture2D
    fun setText(text: String, fontSize: Float) {
        // Creates a new mutable bitmap based on text and font size
        val bitmap = BitmapHelper.createBitmap(text, fontSize)
        mWidth = bitmap.width
        mHeight = bitmap.height
        XGLTextureHelper.loadImageIntoMutableTexture(mHandle, bitmap, mInternalFormat)
        bitmap.recycle()

        Timber.i("set new text($text) to Texture, size=($mWidth, $mHeight)")
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
        // Set active texture unit
        glActiveTexture(XGLTextureSlots.getTextureUnit(slot))

        // Bind this texture with above active texture unit
        glBindTexture(GL_TEXTURE_2D, mHandle)
    }

    open fun unbind() {
        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0)
    }



}