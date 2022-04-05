package com.focus617.tankwar.scene.components

import com.focus617.platform.helper.BitmapHelper.rotate
import com.focus617.tankwar.scene.GameConstant
import com.focus617.tankwar.scene.GameConstant.KEY_ENEMY_FIRE_STRATEGY
import com.focus617.tankwar.scene.GameConstant.KEY_FRIEND_FIRE_STRATEGY
import com.focus617.tankwar.scene.GameConstant.KEY_TANK_SPEED
import com.focus617.tankwar.scene.GameScene
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.IfScene
import com.focus617.tankwar.scene.base.MovableNode
import com.focus617.tankwar.scene.tankFireStrategy.DefaultFireStrategy
import com.focus617.tankwar.scene.tankFireStrategy.FireStrategy
import timber.log.Timber
import java.util.*

class Tank(
    name: String,
    scene: IfScene,
    private val isEnemy: Boolean = true,
    override var x: Int,
    override var y: Int,
    override var dir: Dir
) : MovableNode(scene) {

    private val random = Random()

    override var speed: Int =
        (scene as GameScene).properties?.getProperty(KEY_TANK_SPEED)?.toInt() ?: 10

    var fs: FireStrategy = DefaultFireStrategy()

    init {
        previousX = x
        previousY = y

        // 从配置文件中读取FireStrategy
        val key = if (isEnemy) KEY_ENEMY_FIRE_STRATEGY else KEY_FRIEND_FIRE_STRATEGY

        var fsName = (scene as GameScene).properties?.getProperty(key)?.toString()
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

    // 通过对象类型，找到Scene中的Bitmap
    override fun findBitmap() {
        bitmap = when (isEnemy) {
            true -> scene.bitmapRepository[GameConstant.TANK_ENEMY_1]!!.rotate(dir.rotateDegrees)
            false -> scene.bitmapRepository[GameConstant.TANK_MINE]!!.rotate(dir.rotateDegrees)
        }
    }

    override fun moveForward() {
        // 坦克转向规则：7%概率随机改变方向
        if (random.nextInt(100) > 93) {
            this.randomDir()
        }

        if (random.nextInt(100) > 97) {
            this.fire()
        }

        super.moveForward()
    }


    // 开炮
    private fun fire() {
        fs.fire(this)
    }

    // 随机改变方向
    private fun randomDir() {
        val choiceNum = Dir.values().size
        this.dir = Dir.values()[random.nextInt(choiceNum)]
    }

}
