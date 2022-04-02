package com.focus617.tankwar.scene.components.tank

import com.focus617.tankwar.scene.GameScene
import com.focus617.tankwar.scene.base.Dir

class FourDirFireStrategy : FireStrategy {

    override fun fire(tank: Tank) {
        (tank.scene as GameScene).addBullet(tank.xPos, tank.yPos - 2, Dir.UP)
        tank.scene.addBullet(tank.xPos, tank.yPos + 2, Dir.DOWN)
        tank.scene.addBullet(tank.xPos - 2, tank.yPos, Dir.LEFT)
        tank.scene.addBullet(tank.xPos + 2, tank.yPos, Dir.RIGHT)
    }

}