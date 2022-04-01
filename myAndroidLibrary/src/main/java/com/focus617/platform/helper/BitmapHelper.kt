package com.focus617.platform.helper

import android.graphics.Bitmap
import android.graphics.Matrix

object BitmapHelper {

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

}