package com.focus617.tankwar.scene.components

import com.focus617.tankwar.scene.GameConstant
import com.focus617.tankwar.scene.base.IfScene
import com.focus617.tankwar.scene.base.Node

class BrickWall(
    scene: IfScene,
    override var x: Int,
    override var y: Int
) : Node(scene) {

    // 通过对象类型，找到Scene中的Bitmap
    override fun findBitmap() {
        bitmap = scene.bitmapRepository[GameConstant.BRICK_WALL]!!
    }
}