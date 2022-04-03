package com.focus617.tankwar.scene.tankFireStrategy

import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.GameScene
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.components.Tank

class FourDirFireStrategy : FireStrategy {

    override fun fire(tank: Tank) {
        val gap = GameConfig.BLOCK_NUM_W + 10

        (tank.scene as GameScene).addBullet(tank.x, tank.y - gap, Dir.UP)
        tank.scene.addBullet(tank.x, tank.y + gap, Dir.DOWN)
        tank.scene.addBullet(tank.x - gap, tank.y, Dir.LEFT)
        tank.scene.addBullet(tank.x + gap, tank.y, Dir.RIGHT)
    }

}