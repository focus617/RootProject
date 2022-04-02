package com.focus617.tankwar.scene.components.tank

import android.content.Context
import com.focus617.platform.config_util.PropertiesUtil
import com.focus617.platform.helper.BitmapHelper.rotate
import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.GameConstant
import com.focus617.tankwar.scene.GameConstant.KEY_ENEMY_FIRE_STRATEGY
import com.focus617.tankwar.scene.GameConstant.KEY_FRIEND_FIRE_STRATEGY
import com.focus617.tankwar.scene.GameConstant.KEY_TANK_SPEED
import com.focus617.tankwar.scene.GameScene
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.IfScene
import com.focus617.tankwar.scene.base.MovableNode
import timber.log.Timber
import java.util.*

class Tank(
    name: String,
    val context: Context,
    scene: IfScene,
    private val isEnemy: Boolean = true,
    override var xPos: Int = 0,
    override var yPos: Int = 0,
    override var dir: Dir = Dir.RIGHT
) : MovableNode(name, context, scene) {

    private val random = Random()

    override var speed: Int =
        PropertiesUtil.loadProperties(context)?.getProperty(KEY_TANK_SPEED)?.toInt() ?: 10

    // 通过对象类型，找到Scene中的Bitmap
    override fun findBitmap() {
        bitmap = when (isEnemy) {
            true -> scene.bitmapRepository[GameConstant.TANK_ENEMY_1]!!.rotate(dir.rotateDegrees)
            false -> scene.bitmapRepository[GameConstant.TANK_MINE]!!.rotate(dir.rotateDegrees)
        }
    }

    override fun move() {
        if ((xDelta == 0) || (yDelta == 0)) {
            // 检查坦克转向规则：10%概率随机改变方向
            if (random.nextInt(100) > 90) {
                this.randomDir()
            }
        }
        super.move()
    }

    override fun checkStrategy() {
        // 检查和销毁无效对象
        if (!checkAlive()) return

        // 如果碰到其它坦克,随机改变方向
        checkCollideWithOtherTanks()

        // 如果坦克碰到边界，就掉头
        checkReachBorder()
    }

    // 检查和销毁无效对象
    private fun checkAlive(): Boolean {
        if (!this.isAlive) {
            (scene as GameScene).removeTank(this)
            return false
        }
        return true
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

    private fun checkReachBorderNew() {
        if (x < 2) x = 20
        if (y < GameConfig.BLOCK_WIDTH) y = GameConfig.BLOCK_WIDTH

        if (x > (GameConfig.BLOCK_NUM_W - 1) * GameConfig.BLOCK_WIDTH - 20)
            x = (GameConfig.BLOCK_NUM_W - 1) * GameConfig.BLOCK_WIDTH
        if (y > GameConfig.BLOCK_NUM_H * GameConfig.BLOCK_WIDTH - 20)
            y = (GameConfig.BLOCK_NUM_H - 1) * GameConfig.BLOCK_WIDTH
    }

    // 测试坦克碰撞规则: 如果碰到障碍物，例如其它坦克,就随机改变方向
    private fun checkCollideWithOtherTanks() {
        val tankList = (scene as GameScene).getTanks()
        for (tank in tankList) {
            if ((this != tank) && this.collideWith(tank)) {
                this.randomDir()
                return
            }
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

    var fs: FireStrategy = DefaultFireStrategy()

    init {
        // 从配置文件中读取FireStrategy
        val key = if (isEnemy) KEY_ENEMY_FIRE_STRATEGY else KEY_FRIEND_FIRE_STRATEGY

        var fsName = PropertiesUtil.loadProperties(context)?.getProperty(key)?.toString()
        Timber.i("Tank.init(): load $fsName")

        if (fsName == null) {
            fsName = "com.focus617.tankwar.scene.components.tank.DefaultFireStrategy"
        }

        try {
            fs = Class.forName(fsName).newInstance() as FireStrategy
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    // 开炮
    fun fire() {
        fs.fire(this)
    }

    private fun collideWith(tank: Tank): Boolean = this.rectangle.intersect(tank.rectangle)

}
