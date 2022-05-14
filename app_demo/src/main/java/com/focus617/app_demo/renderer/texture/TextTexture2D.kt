package com.focus617.app_demo.renderer.texture

import android.graphics.*
import android.opengl.GLES31.*
import com.focus617.core.engine.renderer.texture.Texture2D
import java.nio.Buffer

class TextTexture2D(val text: String, val fontSize: Float) : Texture2D("TextTexture$text") {
    private val mHandleBuf = IntArray(1)
    override var mHandle: Int = -1
    var textureIndex: Int = -1    // 在TextureSlots内的Index

    override var mWidth: Int = 0
    override var mHeight: Int = 0

    init {
        // Creates a new mutable bitmap based on text and font size
        val textBitmap = createBitmap(text, fontSize)
        mWidth = textBitmap.width
        mHeight = textBitmap.height
        mHandle = XGLTextureHelper.loadImageIntoTexture(mHandleBuf, textBitmap)
        // Recycle the bitmap, since its data has been loaded into OpenGL.
        textBitmap.recycle()

        // 注册到TextureSlots, 获得TextureUnit Index, 以便ActiveTexture
        textureIndex = XGLTextureSlots.getId(this)
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
        TODO("Not yet implemented")
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
        LOG.info("build textTexture, text=$text, size=($bitmapWidth, $bitmapHeight)")

        return textBitmap
    }
}