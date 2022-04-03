package com.focus617.tankwar.scene.collider

import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.GameScene
import com.focus617.tankwar.scene.base.Node
import com.focus617.tankwar.scene.components.Tank

class TankBorderCollider : Collider {

    override fun collide(node1: Node, node2: Node): Boolean {
        if ((node1 !is Tank) && (node2 !is Tank)) return false

        if (node1 is Tank) checkReachBorder(node1)
        if (node2 is Tank) checkReachBorder(node2)
        return false
    }

    private fun checkReachBorder(tank: Tank) {
        val scene = tank.scene as GameScene
        val mapTop = 0
        val mapBottom = scene.mapHeight - 1
        val mapLeft = 0
        val mapRight = scene.mapWidth - 1

        // 如果坦克开出边界，就停止，等待转向
        if (tank.x < mapLeft) {
            tank.x = mapLeft
        }
        else if (tank.x > mapRight - GameConfig.BLOCK_WIDTH) {
            tank.x = mapRight - GameConfig.BLOCK_WIDTH
        }

        if (tank.y < mapTop) {
            tank.y = mapTop
        }
        else if (tank.y > mapBottom - GameConfig.BLOCK_WIDTH) {
            tank.y = mapBottom - GameConfig.BLOCK_WIDTH
        }
    }

}