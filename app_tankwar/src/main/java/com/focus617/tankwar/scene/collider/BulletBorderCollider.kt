package com.focus617.tankwar.scene.collider

import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.base.Node
import com.focus617.tankwar.scene.components.Bullet

class BulletBorderCollider : Collider {

    override fun collide(node1: Node, node2: Node): Boolean {
        if ((node1 !is Bullet) && (node2 !is Bullet)) return false

        if (node1 is Bullet) checkReachBorder(node1)
        else checkReachBorder(node2 as Bullet)
        return true
    }

    private fun checkReachBorder(bullet: Bullet) {
        val scene = bullet.scene
        val mapTop = scene.rootNode.rect.top
        val mapBottom = scene.rootNode.rect.bottom
        val mapLeft = scene.rootNode.rect.left
        val mapRight = scene.rootNode.rect.right

        // 如果炮弹打出边界,就爆炸并标记需要销毁
        if (bullet.x < mapLeft) {
            bullet.x = mapLeft
            bullet.die()
        }
        else if (bullet.x > mapRight - GameConfig.BLOCK_WIDTH) {
            bullet.x = mapRight - GameConfig.BLOCK_WIDTH
            bullet.die()
        }
        if (bullet.y < mapTop) {
            bullet.y = mapTop
            bullet.die()
        }
        else if (bullet.y > mapBottom - GameConfig.BLOCK_WIDTH) {
            bullet.y = mapBottom - GameConfig.BLOCK_WIDTH
            bullet.die()
        }
    }

}