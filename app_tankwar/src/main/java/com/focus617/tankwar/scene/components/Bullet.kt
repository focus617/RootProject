package com.focus617.tankwar.scene.components

import android.content.Context
import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.GameConstant
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.IfScene
import com.focus617.tankwar.scene.base.MovableNode

class Bullet(
    name: String,
    context: Context,
    scene: IfScene,
    override var xPos: Int,
    override var yPos: Int,
    override var dir: Dir
) : MovableNode(name, context, scene) {

    override var speed: Int = GameConfig.BULLET_SPEED

    // 通过对象类型，找到Scene中的Bitmap
    override fun findBitmap() {
        bitmap = when (dir) {
            Dir.UP -> scene.bitmapRepository[GameConstant.BULLET_UP]!!
            Dir.DOWN -> scene.bitmapRepository[GameConstant.BULLET_DOWN]!!
            Dir.LEFT -> scene.bitmapRepository[GameConstant.BULLET_LEFT]!!
            Dir.RIGHT -> scene.bitmapRepository[GameConstant.BULLET_RIGHT]!!
        }
    }

    // 如果炮弹打出边界，就从集合中删除
    override fun checkDir() {
        if ((xPos < 0) || (xPos > GameConfig.BLOCK_NUM_W - 1) ||
            (yPos < 0) || (yPos > GameConfig.BLOCK_NUM_H - 1)
        ) {
            live = false
        }

        super.checkDir()
    }

}