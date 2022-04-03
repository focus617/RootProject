package com.focus617.tankwar.scene.collider

import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.GameScene
import com.focus617.tankwar.scene.base.Node
import com.focus617.tankwar.scene.components.Bullet

class BulletBorderCollider : Collider {

    override fun collide(node1: Node, node2: Node): Boolean {
        if ((node1 !is Bullet) && (node2 !is Bullet)) return false

        if (node2 is Bullet) checkReachBorder(node2)

        return if (node1 is Bullet) checkReachBorder(node1)
        else false

    }

    private fun checkReachBorder(bullet: Bullet): Boolean {
        val scene = bullet.scene as GameScene
        val mapTop = 0
        val mapBottom = scene.mapHeight - 1
        val mapLeft = 0
        val mapRight = scene.mapWidth - 1

        // 如果炮弹打出边界,就爆炸并标记需要销毁
        if (bullet.x < mapLeft) {
            bullet.x = mapLeft
            bullet.die()
            return true
        }
        else if (bullet.x > mapRight - GameConfig.BLOCK_WIDTH) {
            bullet.x = mapRight - GameConfig.BLOCK_WIDTH
            bullet.die()
            return true
        }
        if (bullet.y < mapTop) {
            bullet.y = mapTop
            bullet.die()
            return true
        }
        else if (bullet.y > mapBottom - GameConfig.BLOCK_WIDTH) {
            bullet.y = mapBottom - GameConfig.BLOCK_WIDTH
            bullet.die()
            return true
        }
        return false
    }

}