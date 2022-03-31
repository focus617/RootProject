package com.focus617.tankwar.scene.components

import android.content.Context
import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.GameConstant
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.IfScene
import com.focus617.tankwar.scene.base.MovableNode

class Tank(
    name: String,
    val context: Context,
    scene: IfScene,
    override var xPos: Int = 0,
    override var yPos: Int = 0,
    override var dir: Dir = Dir.RIGHT
) : MovableNode(name, context, scene) {

    override var speed: Int = GameConfig.TANK_SPEED

    // 通过对象类型，找到Scene中的Bitmap
    override fun findBitmap() {
        bitmap = when (dir) {
            Dir.UP -> scene.bitmapRepository[GameConstant.TANK_GOOD_UP]!!
            Dir.DOWN -> scene.bitmapRepository[GameConstant.TANK_GOOD_DOWN]!!
            Dir.LEFT -> scene.bitmapRepository[GameConstant.TANK_GOOD_LEFT]!!
            Dir.RIGHT -> scene.bitmapRepository[GameConstant.TANK_GOOD_RIGHT]!!
        }
    }


    override fun checkDir() {
        // 检查坦克转向规则：如果坦克碰到边界，就掉头
        if ((xPos < 1) && (Dir.LEFT == dir)) {
            dir = Dir.RIGHT
            xPos = 0
            fire()
        } else if ((xPos > GameConfig.BLOCK_NUM_W - 2) && (Dir.RIGHT == dir)) {
            dir = Dir.LEFT
            xPos = GameConfig.BLOCK_NUM_W - 1
            fire()
        }

        if ((yPos < 1) && (Dir.UP == dir)) {
            dir = Dir.DOWN
            yPos = 0
            fire()
        } else if ((yPos > GameConfig.BLOCK_NUM_H - 2) && (Dir.DOWN == dir)) {
            dir = Dir.UP
            yPos = GameConfig.BLOCK_NUM_H - 1
            fire()
        }

        // 销毁死亡的坦克
        super.checkDir()
    }

    // 开炮
    fun fire() {
        when (dir) {
            Dir.UP -> scene.rootNode.add(Bullet("bullet", context, scene, xPos, yPos - 1, dir))
            Dir.DOWN -> scene.rootNode.add(Bullet("bullet", context, scene, xPos, yPos + 1, dir))
            Dir.LEFT -> scene.rootNode.add(Bullet("bullet", context, scene, xPos - 1, yPos, dir))
            Dir.RIGHT -> scene.rootNode.add(Bullet("bullet", context, scene, xPos + 1, yPos, dir))
        }

    }
}
