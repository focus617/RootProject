package com.focus617.tankwar.scene.components

import android.content.Context
import android.graphics.Rect
import com.focus617.platform.helper.BitmapHelper.rotate
import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.GameConstant
import com.focus617.tankwar.scene.GameScene
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.IfScene
import com.focus617.tankwar.scene.base.MovableNode
import kotlin.random.Random

class Tank(
    name: String,
    val context: Context,
    scene: IfScene,
    private val isEnemy: Boolean = true,
    override var xPos: Int = 0,
    override var yPos: Int = 0,
    override var dir: Dir = Dir.RIGHT
) : MovableNode(name, context, scene) {

    override var speed: Int = GameConfig.TANK_SPEED

    // 通过对象类型，找到Scene中的Bitmap
    override fun findBitmap() {
        bitmap = when (isEnemy) {
            true -> scene.bitmapRepository[GameConstant.TANK_ENEMY_1]!!.rotate(dir.rotateDegrees)
            false -> scene.bitmapRepository[GameConstant.TANK_MINE]!!.rotate(dir.rotateDegrees)
        }
    }

    override fun checkStrategy() {
        // 如果碰到其它坦克,随机改变方向
        checkCollideWithOtherTanks()

        // 如果走了一定距离,随机改变方向
        checkRandomDir()

        // 如果坦克碰到边界，就掉头
        checkReachBorder()
    }

    // 检查坦克转向规则：如果坦克碰到边界，就掉头，同时开火
    private fun checkReachBorder() {

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
    }

    // 测试坦克碰撞规则: 如果碰到障碍物，例如其它坦克,就随机改变方向
    private fun checkCollideWithOtherTanks() {

        val tankList = scene.rootNode.getTanks()
        for (tank in tankList) {
            if ((this != tank) && this.collideWith(tank)) {
                this.randomDir()
                return
            }
        }
    }

    private val random = Random(100)

    // 检查坦克转向规则：3%概率随机改变方向
    private fun checkRandomDir() {
        if (random.nextInt(100) > 97) {
            this.randomDir()
        }
    }

    // 随机改变方向
    private fun randomDir() {
        val choiceNum = Dir.values().size
        this.dir = Dir.values()[random.nextInt(choiceNum)]
    }

    //坦克被炮弹击中后爆炸
    override fun die() {
        super.die()
        explode()
    }

    // 爆炸
    private fun explode() {
        when (dir) {
            Dir.UP -> (scene as GameScene).addExplode(xPos, yPos - 1)
            Dir.DOWN -> (scene as GameScene).addExplode(xPos, yPos + 1)
            Dir.LEFT -> (scene as GameScene).addExplode(xPos - 1, yPos)
            Dir.RIGHT -> (scene as GameScene).addExplode(xPos + 1, yPos)
        }
    }

    // 开炮
    fun fire() {
        when (dir) {
            Dir.UP -> (scene as GameScene).addBullet(xPos, yPos - 1, dir)
            Dir.DOWN -> (scene as GameScene).addBullet(xPos, yPos + 1, dir)
            Dir.LEFT -> (scene as GameScene).addBullet(xPos - 1, yPos, dir)
            Dir.RIGHT -> (scene as GameScene).addBullet(xPos + 1, yPos, dir)
        }
    }

    private fun collideWith(tank: Tank): Boolean {

        val rect1 = Rect(
            this.x, this.y,
            this.x + GameConfig.BLOCK_WIDTH, this.y + GameConfig.BLOCK_WIDTH
        )
        val rect2 = Rect(
            tank.x, tank.y,
            tank.x + GameConfig.BLOCK_WIDTH, tank.y + GameConfig.BLOCK_WIDTH
        )

        return rect1.intersect(rect2)
    }

}
