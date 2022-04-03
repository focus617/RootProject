package com.focus617.tankwar.scene.collider

import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.base.Node
import com.focus617.tankwar.scene.components.Tank

class TankBorderCollider : Collider {

    override fun collide(node1: Node, node2: Node): Boolean {
        if ((node1 !is Tank) && (node2 !is Tank)) return false

        if (node1 is Tank) checkReachBorder(node1)
        else checkReachBorder(node2 as Tank)
        return true
    }

    private fun checkReachBorder(tank: Tank) {
        val scene = tank.scene
        val mapTop = scene.rootNode.rect.top
        val mapBottom = scene.rootNode.rect.bottom
        val mapLeft = scene.rootNode.rect.left
        val mapRight = scene.rootNode.rect.right

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