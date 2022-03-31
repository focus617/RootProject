package com.focus617.tankwar.scene.components

import android.content.Context
import com.focus617.tankwar.R
import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.MovableNode
import com.focus617.tankwar.scene.base.RootNode

class Bullet(
    name: String,
    context: Context,
    scene: RootNode,
    override var xPos: Int,
    override var yPos: Int,
    override var dir: Dir
) : MovableNode(name, context, scene) {

    override var speed: Int = GameConfig.BULLET_SPEED



    init {
        initBitmap(R.drawable.ic_bullet_up)
    }

    // 如果炮弹打出边界，就从集合中删除
    override fun checkDir() {
        if ((xPos < 0) || (xPos > GameConfig.BLOCK_NUM_W - 1) ||
            (yPos < 0) || (yPos > GameConfig.BLOCK_NUM_H - 1)
        ) {
            live = false
        }

        super.checkDir()
    }

}