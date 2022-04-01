package com.focus617.tankwar.scene.components

import android.content.Context
import android.graphics.Rect
import com.focus617.platform.helper.BitmapHelper.rotate
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
        bitmap = scene.bitmapRepository[GameConstant.BULLET]!!.rotate(dir.rotateDegrees)
    }

    // 如果炮弹打出边界，就从集合中删除
    override fun checkStrategy() {
        if ((xPos < 0) || (xPos > GameConfig.BLOCK_NUM_W - 1) ||
            (yPos < 0) || (yPos > GameConfig.BLOCK_NUM_H - 1)
        ) {
            this.die()
        }

        // 测试炮弹是否打中坦克
        val tankList = scene.rootNode.getTanks()
        for (tank in tankList) collideWith(tank)

    }

    private fun collideWith(tank: Tank) {
        val rect1 = Rect(
            this.x, this.y,
            this.x + GameConfig.BLOCK_WIDTH, this.y + GameConfig.BLOCK_WIDTH
        )
        val rect2 = Rect(
            tank.x, tank.y,
            tank.x + GameConfig.BLOCK_WIDTH, tank.y + GameConfig.BLOCK_WIDTH
        )

        if (rect1.intersect(rect2)) {
            tank.die()
            this.die()
        }
    }

}