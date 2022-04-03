package com.focus617.tankwar.scene.collider

import com.focus617.tankwar.scene.base.Node
import java.util.*

class ColliderChain : Collider {

    private val colliders = LinkedList<Collider>()

    init {
        // TODO: move below to properties configuration
        add(BulletTankCollider())
        add(BulletBrickWallCollider())
        add(BulletStoneWallCollider())
        add(BulletBorderCollider())
        add(TankBorderCollider())
        add(TankTankCollider())
        add(TankWallCollider())
    }

    fun add(c: Collider) {
        colliders.add(c)
    }

    override fun collide(node1: Node, node2: Node): Boolean {
        for (collider in colliders) {
            if(collider.collide(node1, node2))
                return true
        }
        return false
    }

}