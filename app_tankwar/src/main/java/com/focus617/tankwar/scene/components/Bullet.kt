package com.focus617.tankwar.scene.components

import com.focus617.platform.helper.BitmapHelper.rotate
import com.focus617.tankwar.scene.GameConstant
import com.focus617.tankwar.scene.GameConstant.KEY_BULLET_SPEED
import com.focus617.tankwar.scene.GameScene
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.IfScene
import com.focus617.tankwar.scene.base.MovableNode
import timber.log.Timber

class Bullet(
    scene: IfScene,
    override var x: Int,
    override var y: Int,
    override var dir: Dir
) : MovableNode(scene) {

    override var speed: Int =
        (scene as GameScene).properties?.getProperty(KEY_BULLET_SPEED)?.toInt() ?: 40

    init {
        Timber.i("Bullet.speed = $speed")
    }

    // 通过对象类型，找到Scene中的Bitmap
    override fun findBitmap() {
        bitmap = scene.bitmapRepository[GameConstant.BULLET]!!.rotate(dir.rotateDegrees)
    }

    override fun checkStrategy() {
        // 检查和销毁无效对象，爆炸并将炮弹从集合中删除
        if (!this.isAlive) {
            (scene as GameScene).addExplode(x, y)      // 爆炸
        }
        super.checkStrategy()
    }

}