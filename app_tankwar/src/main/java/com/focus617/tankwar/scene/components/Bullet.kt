package com.focus617.tankwar.scene.components

import android.content.Context
import com.focus617.platform.config_util.PropertiesUtil
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
    context: Context,
    scene: IfScene,
    override var xPos: Int,
    override var yPos: Int,
    override var dir: Dir
) : MovableNode(name, context, scene) {

    override var speed: Int =
        PropertiesUtil.loadProperties(context)?.getProperty(KEY_BULLET_SPEED)?.toInt() ?: 10

    // 通过对象类型，找到Scene中的Bitmap
    override fun findBitmap() {
        bitmap = scene.bitmapRepository[GameConstant.BULLET]!!.rotate(dir.rotateDegrees)
    }

    override fun checkStrategy() {
        // 如果炮弹打出边界，就从集合中删除
        if ((xPos < 0) || (xPos > GameConfig.BLOCK_NUM_W - 1) ||
            (yPos < 0) || (yPos > GameConfig.BLOCK_NUM_H - 1)
        ) {
            this.die()
        }

        // 测试炮弹是否打中坦克
        if (this.isAlive) {
            val tankList = (scene as GameScene).getTanks()
            for (tank in tankList) {
                if (this.isAlive) collideWith(tank)
                else break
            }
        }

        // 检查和销毁无效对象
        if (!this.isAlive) {
            (scene as GameScene).removeBullet(this)
            return
        }
    }



    private fun collideWith(tank: Tank) {
        if (this.rectangle.intersect(tank.rectangle)) {
            tank.die()
            this.die()
        }
    }

}