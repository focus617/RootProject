package com.focus617.app_demo.renderer.text

import android.graphics.*
import android.opengl.GLES31.*
import com.focus617.app_demo.engine.XGLContext
import com.focus617.app_demo.renderer.texture.XGLTextureSlots
import com.focus617.core.engine.renderer.texture.Texture2D
import java.nio.Buffer
import java.nio.ByteBuffer

class TextTexture2D(width: Int, height: Int) : Texture2D("TextTexture") {
    private val mHandleBuf = IntArray(1)
    override var mHandle: Int = -1
    var textureIndex: Int = -1    // 在TextureSlots内的Index

    override var mWidth: Int = 0
    override var mHeight: Int = 0

    private var mInternalFormat: Int = GL_RGBA8
    private var mDataFormat: Int = GL_RGBA

    init {
        mWidth = width
        mHeight = height

        glGenTextures(1, mHandleBuf, 0)
        if (mHandleBuf[0] == 0) {
            LOG.error("Could not generate a new OpenGL texture object.")
        }
        mHandle = mHandleBuf[0]

        // Bind to the texture in OpenGL
//        glBindTexture(GL_TEXTURE_2D, mHandle)

        // Allocate texture storage
//        glTexStorage2D(GL_TEXTURE_2D, 1, mInternalFormat, mWidth, mHeight)
//        XGLContext.checkGLError("glTexStorage2D")

        // 注册到TextureSlots, 获得TextureUnit Index, 以便ActiveTexture
        textureIndex = XGLTextureSlots.getId(this)

        // Unbind from the texture.
//        glBindTexture(GL_TEXTURE_2D, 0)
    }

    override fun bind(slot: Int) {
        if (textureIndex != -1) {
            // Set active texture unit
            glActiveTexture(XGLTextureSlots.getTextureUnit(textureIndex))
            // Bind this texture with above active texture unit
            glBindTexture(GL_TEXTURE_2D, mHandle)
        }
    }

    fun unbind() {
        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    override fun close() {
        glDeleteTextures(1, mHandleBuf, 0)
    }

    override fun setData(data: Buffer, size: Int) {
        val bpp = if (mDataFormat == GL_RGBA) 4 else 3
        require(size == (mWidth * mHeight * bpp)) {
            "Data must be entire texture!"
        }
        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, mHandle)
        // Load the buffer into the bound texture.
        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            GL_RGBA,
            mWidth,
            mHeight,
            0,
            mDataFormat,
            GL_UNSIGNED_BYTE,
            data
        )
        XGLContext.checkGLError("glTexImage2D")

        // Note: Following code may cause an error to be reported in the
        // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
        // Failed to generate texture mipmap levels (error=3)
        // No OpenGL error will be encountered (glGetError() will return
        // 0). If this happens, just squash the source image to be
        // square. It will look the same because of texture coordinates,
        // and mipmap generation will work.
        glGenerateMipmap(GL_TEXTURE_2D)

        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    fun setText(text: String, fontSize: Float){
        // Creates a new mutable bitmap based on text and font size
        val bitmap = createBitmap(text, fontSize)
        mWidth = bitmap.width
        mHeight = bitmap.height

        // Check the bitmap format
        setDataFormat(bitmap)

        val bpp = if (mDataFormat == GL_RGBA) 4 else 3
        val size = bitmap.width * bitmap.height * bpp
        val byteBuf = ByteBuffer.allocate(size)
        bitmap.copyPixelsToBuffer(byteBuf)
        byteBuf.position(0)
        // Recycle the bitmap, since its data has been loaded into buffer.
        bitmap.recycle()

        LOG.info("set new text to Texture, text=$text, size=($mWidth, $mHeight)")

        setData(byteBuf, size)
    }

    private fun createBitmap(text: String, fontSize: Float): Bitmap {
        var aFontSize = fontSize
        if (aFontSize < 8.0f) aFontSize = 8.0f
        if (aFontSize > 500.0f) aFontSize = 500.0f

        val textPaint = Paint()
        textPaint.textSize = aFontSize
        textPaint.isFakeBoldText = false
        textPaint.isAntiAlias = true
        textPaint.setARGB(255, 255, 255, 255)
        // If a hinting is available on the platform you are developing,
        // you should enable it (uncomment the line below).
//        textPaint.hinting = Paint.HINTING_ON;
        textPaint.isSubpixelText = true
        textPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SCREEN)

        val realTextWidth: Float = textPaint.measureText(text)

        // Creates a new mutable bitmap, with 128px of width and height
        val bitmapWidth = (realTextWidth + 2.0f).toInt()
        val bitmapHeight = aFontSize.toInt() + 2

        val textBitmap: Bitmap =
            Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)

        textBitmap.eraseColor(Color.argb(0, 255, 255, 255))

        // Creates a new canvas that will draw into a bitmap instead of rendering into the screen
        val bitmapCanvas = Canvas(textBitmap)

        // Set start drawing position to [1, base_line_position]
        // The base_line_position may vary from one font to another
        // but it usually is equal to 75% of font size (height).
        bitmapCanvas.drawText(text, 1f, 1.0f + aFontSize * 0.75f, textPaint)

        return textBitmap
    }

    private fun setDataFormat(textBitmap: Bitmap) {
        mDataFormat = when (textBitmap.config) {
            Bitmap.Config.ARGB_8888 -> GL_RGBA
            else -> {
                LOG.error("Unknown bitmap format")
                -1
            }
        }
    }
}