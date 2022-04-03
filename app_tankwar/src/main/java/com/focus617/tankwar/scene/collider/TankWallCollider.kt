package com.focus617.tankwar.scene.collider

import com.focus617.tankwar.scene.base.Node
import com.focus617.tankwar.scene.components.BrickWall
import com.focus617.tankwar.scene.components.StoneWall
import com.focus617.tankwar.scene.components.Tank

// 测试坦克碰撞规则: 如果碰到障碍物，例如其它坦克,就随机改变方向
class TankWallCollider() : Collider {

    override fun collide(node1: Node, node2: Node): Boolean {
        if ((node1 is Tank) && ((node2 is BrickWall) || (node2 is StoneWall))) {
            if (node1.rect.intersect(node2.rect))
                node1.back()
        }
        if ((node2 is Tank) && ((node1 is BrickWall) || (node1 is StoneWall))) {
            if (node2.rect.intersect(node1.rect))
                node2.back()
        }

        return false
    }

}