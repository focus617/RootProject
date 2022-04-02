package com.focus617.tankwar.scene.base

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.cor.BulletTankCollider
import com.focus617.tankwar.scene.cor.Collider
import com.focus617.tankwar.scene.cor.ColliderChain
import com.focus617.tankwar.scene.cor.TankTankCollider

class RootNode(name: String) : Composite(name) {

    private val paint = Paint()    // 画笔

    override fun drawComposite(canvas: Canvas) {

        with(paint) {
            color = Color.BLACK                  //设置画笔颜色
            style = Paint.Style.FILL             //设置填充样式
            strokeWidth = 20F                    //设置画笔宽度
            isAntiAlias = true
        }

        canvas.run {
            // 初始化画布并设置画布背景
            drawColor(Color.WHITE)

            val halfWidth = GameConfig.BLOCK_WIDTH * GameConfig.BLOCK_NUM_W / 2
            val halfHeight = GameConfig.BLOCK_WIDTH * GameConfig.BLOCK_NUM_H / 2

            drawRect(
                (width / 2 - halfWidth).toFloat(), (height / 2 - halfHeight).toFloat(),
                (width / 2 + halfWidth).toFloat(), (height / 2 + halfHeight).toFloat(),
                paint
            )
        }
    }

    private val chain: ColliderChain = ColliderChain()

    override fun refreshCompositeData() {
        // 比较对象两两之间的互动
        for (i in 0 until children.size)
            for (j in 0 until children.size) run {
                val o1: Node = children[i] as Node
                val o2: Node = children[j] as Node
                chain.collide(o1, o2)
            }
    }
}