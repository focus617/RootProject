package com.focus617.tankwar.scene.collider

import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.base.Node
import com.focus617.tankwar.scene.components.BrickWall
import com.focus617.tankwar.scene.components.Bullet
import com.focus617.tankwar.scene.components.StoneWall
import com.focus617.tankwar.scene.components.Tank

class BulletStoneWallCollider : Collider {

    override fun collide(node1: Node, node2: Node): Boolean {
        if ((node1 is Bullet) && (node2 is StoneWall)) {
            if (node1.rect.intersect(node2.rect)) {
                node1.die()
                return true
            }
        } else if ((node1 is StoneWall) && (node2 is Bullet)) {
            if (node2.rect.intersect(node1.rect)) {
                node2.die()
                return true
            }
        }
        return false
    }
}