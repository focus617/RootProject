package com.focus617.tankwar.scene.collider

import com.focus617.tankwar.scene.base.Node
import com.focus617.tankwar.scene.components.Bullet
import com.focus617.tankwar.scene.components.Tank

// 测试炮弹是否打中坦克
class BulletTankCollider() : Collider {

    override fun collide(node1: Node, node2: Node): Boolean {

        if ((node1 is Bullet) && (node2 is Tank)) {
            if (node1.rect.intersect(node2.rect)) {
                node1.die()
                node2.die()
                return true
            }
        } else if ((node1 is Tank) && (node2 is Bullet)) {
            if (node2.rect.intersect(node1.rect)) {
                node1.die()
                node2.die()
                return true
            }
        }
        return false
    }

}