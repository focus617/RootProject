package com.focus617.tankwar.scene.components

import com.focus617.platform.helper.BitmapHelper.rotate
import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.GameConstant
import com.focus617.tankwar.scene.GameConstant.KEY_BULLET_SPEED
import com.focus617.tankwar.scene.GameScene
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.IfScene
import com.focus617.tankwar.scene.base.MovableNode

class Bullet(
    name: String,
    scene: IfScene,
    override var x: Int,
    override var y: Int,
    override var dir: Dir
) : MovableNode(name, scene) {

    override var speed: Int =
        (scene as GameScene).properties?.getProperty(KEY_BULLET_SPEED)?.toInt() ?: 10

    // 通过对象类型，找到Scene中的Bitmap
    override fun findBitmap() {
        bitmap = scene.bitmapRepository[GameConstant.BULLET]!!.rotate(dir.rotateDegrees)
    }

    override fun checkStrategy() {
        val mapTop = (scene as GameScene).rootNode.rect.top
        val mapBottom = scene.rootNode.rect.bottom
        val mapLeft = scene.rootNode.rect.left
        val mapRight = scene.rootNode.rect.right

        // 如果炮弹打出边界,就爆炸并标记需要销毁
        if ((x < mapLeft) || (x > mapRight - GameConfig.BLOCK_WIDTH) ||
            (y < mapTop) || (y > mapBottom - GameConfig.BLOCK_WIDTH)
        ) {
            this.die()

            // 爆炸
            when (dir) {
                Dir.UP -> scene.addExplode(x, mapTop)
                Dir.DOWN -> scene.addExplode(x, mapBottom - GameConfig.BLOCK_WIDTH)
                Dir.LEFT -> scene.addExplode(mapLeft, y)
                Dir.RIGHT -> scene.addExplode(mapRight - GameConfig.BLOCK_WIDTH, y)
            }
        }

        // 检查和销毁无效对象，爆炸并将炮弹从集合中删除
        if (!this.isAlive) {
            scene.removeBullet(this)
            return
        }
    }

}