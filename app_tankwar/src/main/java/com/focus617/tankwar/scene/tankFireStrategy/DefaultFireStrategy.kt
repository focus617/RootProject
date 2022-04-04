package com.focus617.tankwar.scene.tankFireStrategy

import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.GameScene
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.components.Tank

class DefaultFireStrategy : FireStrategy {

    override fun fire(tank: Tank) {
        val gap = GameConfig.BLOCK_WIDTH + 50

        when (tank.dir) {
            Dir.UP -> (tank.scene as GameScene).addBullet(tank.x, tank.y - gap, tank.dir)
            Dir.DOWN -> (tank.scene as GameScene).addBullet(tank.x, tank.y + gap, tank.dir)
            Dir.LEFT -> (tank.scene as GameScene).addBullet(tank.x - gap, tank.y, tank.dir)
            Dir.RIGHT -> (tank.scene as GameScene).addBullet(tank.x + gap, tank.y, tank.dir)
        }
    }

}