package com.focus617.tankwar.scene.collider

import com.focus617.tankwar.scene.base.Node
import com.focus617.tankwar.scene.components.Tank

// 测试坦克碰撞规则: 如果碰到障碍物，例如其它坦克,就随机改变方向
class TankTankCollider() : Collider {

    override fun collide(node1: Node, node2: Node): Boolean {
        if ((node1 is Tank) && (node2 is Tank)) {
            if (node1.rect.intersect(node2.rect)) {
                node1.x = node1.previousX
                node1.y = node1.previousY
                node2.x = node2.previousX
                node2.y = node2.previousY
            }
        }
        return false
    }

}