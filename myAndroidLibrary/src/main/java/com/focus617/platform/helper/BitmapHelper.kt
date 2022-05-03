package com.focus617.platform.helper

import android.content.Context
import android.graphics.*
import timber.log.Timber
import java.io.IOException

object BitmapHelper {
    val TAG = "BitmapHelper"

    /**
     * Loads a bitmap from a resource ID; throw exception if the load failed.
     *
     * @param context
     * @param resourceId
     * @return Bitmap
     */
    fun bitmapLoader(context: Context, resourceId: Int): Bitmap {
        Timber.i("$TAG: load texture from resource: $resourceId")

        val options = BitmapFactory.Options()
        options.inScaled = false

        // Read in the resource
        val bitmap = BitmapFactory.decodeResource(
            context.resources, resourceId, options
        )
        if (bitmap == null) {
            Timber.e("$TAG: Resource ID $resourceId could not be decoded.")
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
        Timber.i("$TAG: load bitmap from file: $filePath")

        var bitmap: Bitmap? = null

        val options = BitmapFactory.Options()
        options.inScaled = false

        try {
            val inputStream = context.resources.assets.open(filePath)
            // Read in the resource
            bitmap = BitmapFactory.decodeStream(inputStream)
            if (bitmap == null) {
                Timber.e("$TAG: $filePath could not be decoded.")
            }
        } catch (e: IOException) {
            throw RuntimeException("Could not open shader file: $filePath $ e")
        }
        return bitmap
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