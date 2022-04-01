package com.focus617.platform.helper

import android.content.res.Resources
import android.graphics.*

object BitmapHelper {

    fun bitmapLoader(resource: Resources, resourceId: Int): Bitmap =
        BitmapFactory.decodeResource(resource, resourceId)

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

}