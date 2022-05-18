package com.focus617.platform.helper

import android.content.Context
import android.graphics.*
import timber.log.Timber
import java.io.IOException

object BitmapHelper {

    /**
     * Loads a bitmap from a resource ID; throw exception if the load failed.
     *
     * @param context
     * @param resourceId
     * @return Bitmap
     */
    fun bitmapLoader(context: Context, resourceId: Int): Bitmap {
        Timber.i("load texture from resource: $resourceId")

        val options = BitmapFactory.Options()
        options.inScaled = false

        // Read in the resource
        val bitmap = BitmapFactory.decodeResource(
            context.resources, resourceId, options
        )
        if (bitmap == null) {
            Timber.e("Resource ID $resourceId could not be decoded.")
        }
        return bitmap
    }

    /**
     * Loads a bitmap from a file; throw exception if the load failed.
     *
     * @param context
     * @param filePath
     * @return Bitmap
     */
    fun bitmapLoader(context: Context, filePath: String): Bitmap{
        Timber.i("load bitmap from file: $filePath")

        var bitmap: Bitmap? = null

        val options = BitmapFactory.Options()
        options.inScaled = false

        try {
            val inputStream = context.resources.assets.open(filePath)
            // Read in the resource
            bitmap = BitmapFactory.decodeStream(inputStream)
            if (bitmap == null) {
                Timber.e("$filePath could not be decoded.")
            }
        } catch (e: IOException) {
            throw RuntimeException("Could not open shader file: $filePath $ e")
        }
        return bitmap
    }

    // Create bitmap for text with font size setting
    fun createBitmap(text: String, fontSize: Float): Bitmap {
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

    //Bitmap 图片的翻转
    fun Bitmap.convert(sx: Float, sy: Float): Bitmap {
        val matrix = Matrix()
        matrix.postScale(sx, sy)
        return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
    }

    //Bitmap,degrees: 待旋转的图片和角度
    fun Bitmap.rotate(degrees: Float): Bitmap {

        val matrix = Matrix()

        //设置旋转矩阵
        matrix.setRotate(
            degrees, (this.width / 2).toFloat(), (this.height / 2).toFloat()
        )

        //生成新的图片
        return Bitmap.createBitmap(
            this, 0, 0, this.width, this.height, matrix, true
        )
    }

    fun Bitmap.draw(canvas: Canvas, left: Int, top: Int, right: Int, bottom: Int) {
        // 画笔
        val paint = Paint()

        // 在绘制以前，坐标(x,y)将由具体实现类进行计算更新
        val srcRect = Rect(0, 0, this.width, this.height)
        val destRect = Rect(left, top, right, bottom)
        canvas.drawBitmap(this, srcRect, destRect, paint)
    }

    fun Bitmap.draw(canvas: Canvas, destRect: Rect) {
        // 画笔
        val paint = Paint()

        // 在绘制以前，坐标(x,y)将由具体实现类进行计算更新
        val srcRect = Rect(0, 0, this.width, this.height)
        canvas.drawBitmap(this, srcRect, destRect, paint)
    }

}