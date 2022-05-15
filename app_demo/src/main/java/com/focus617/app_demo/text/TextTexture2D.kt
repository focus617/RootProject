package com.focus617.app_demo.text

import android.graphics.*
import android.opengl.GLES31.*
import com.focus617.app_demo.renderer.texture.XGLTexture2D
import com.focus617.app_demo.renderer.texture.XGLTextureHelper
import com.focus617.app_demo.renderer.texture.XGLTextureSlots

class TextTexture2D : XGLTexture2D("TextTexture") {
    var textureIndex: Int = -1    // 在TextureSlots内的Index

    override fun bind(slot: Int) {
        // 注册到TextureSlots, 获得TextureUnit Index, 以便ActiveTexture
        textureIndex = XGLTextureSlots.getId(this)

        // Set active texture unit
        glActiveTexture(XGLTextureSlots.getTextureUnit(textureIndex))
        // Bind this texture with above active texture unit
        glBindTexture(GL_TEXTURE_2D, mHandle)
    }

    fun setText(text: String, fontSize: Float) {
        // Creates a new mutable bitmap based on text and font size
        val bitmap = createBitmap(text, fontSize)
        mWidth = bitmap.width
        mHeight = bitmap.height
        XGLTextureHelper.loadImageIntoMutableTexture(mHandle, bitmap, mInternalFormat)
        bitmap.recycle()

        LOG.info("set new text($text) to Texture, size=($mWidth, $mHeight)")
    }

    // Create bitmap for text with font size setting
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
}