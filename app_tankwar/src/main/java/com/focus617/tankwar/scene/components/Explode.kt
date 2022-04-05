package com.focus617.tankwar.scene.components

import android.graphics.Canvas
import com.focus617.tankwar.scene.GameConstant
import com.focus617.tankwar.scene.base.IfScene
import com.focus617.tankwar.scene.base.Node

class Explode(
    scene: IfScene,
    override var x: Int,
    override var y: Int
) : Node(scene) {

    private var paintTimes: Int = 1

    // 通过对象类型，找到Scene中的Bitmap
    override fun findBitmap() {
        bitmap = when (paintTimes) {
            1 -> scene.bitmapRepository[GameConstant.EXPLODE_1]!!
            2 -> scene.bitmapRepository[GameConstant.EXPLODE_2]!!
            3 -> scene.bitmapRepository[GameConstant.EXPLODE_3]!!
            4 -> scene.bitmapRepository[GameConstant.EXPLODE_4]!!
            5 -> scene.bitmapRepository[GameConstant.EXPLODE_5]!!
            6 -> scene.bitmapRepository[GameConstant.EXPLODE_6]!!
            7 -> scene.bitmapRepository[GameConstant.EXPLODE_7]!!
            8 -> scene.bitmapRepository[GameConstant.EXPLODE_8]!!
            9 -> scene.bitmapRepository[GameConstant.EXPLODE_9]!!
            10 -> scene.bitmapRepository[GameConstant.EXPLODE_10]!!
            11 -> scene.bitmapRepository[GameConstant.EXPLODE_11]!!
            12 -> scene.bitmapRepository[GameConstant.EXPLODE_12]!!
            13 -> scene.bitmapRepository[GameConstant.EXPLODE_13]!!
            14 -> scene.bitmapRepository[GameConstant.EXPLODE_14]!!
            15 -> scene.bitmapRepository[GameConstant.EXPLODE_15]!!
            else -> scene.bitmapRepository[GameConstant.EXPLODE_16]!!
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        paintTimes++
    }

    override fun checkStrategy() {
        if (paintTimes > 16) {
            this.die()
        }
        super.checkStrategy()
    }
}