package com.focus617.tankwar.scene.collider

import com.focus617.tankwar.scene.base.Node
import com.focus617.tankwar.scene.components.Tank
import timber.log.Timber

// 测试坦克碰撞规则: 如果碰到障碍物，例如其它坦克,就随机改变方向
class TankTankCollider() : Collider {

    override fun collide(node1: Node, node2: Node): Boolean {
        if ((node1 is Tank) && (node2 is Tank)) {

            // TODO: check why below intersect always true?
            if (node1.rect.intersect(node2.rect)) {
                Timber.i("${node1.name} collided with ${node2.name}")
                node1.back()
                node2.back()
            }
        }
        return false
    }

}