package com.focus617.tankwar.scene.components.tank

import com.focus617.tankwar.scene.GameScene
import com.focus617.tankwar.scene.base.Dir

class DefaultFireStrategy: FireStrategy {

    override fun fire(tank: Tank) {
        when (tank.dir) {
            Dir.UP -> (tank.scene as GameScene).addBullet(tank.xPos, tank.yPos - 1, tank.dir)
            Dir.DOWN -> (tank.scene as GameScene).addBullet(tank.xPos, tank.yPos + 1, tank.dir)
            Dir.LEFT -> (tank.scene as GameScene).addBullet(tank.xPos - 1, tank.yPos, tank.dir)
            Dir.RIGHT -> (tank.scene as GameScene).addBullet(tank.xPos + 1, tank.yPos, tank.dir)
        }
    }

}