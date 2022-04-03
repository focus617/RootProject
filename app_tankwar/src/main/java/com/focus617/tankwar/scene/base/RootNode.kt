package com.focus617.tankwar.scene.base

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.collider.ColliderChain

class RootNode(name: String) : Composite(name) {

    private val paint = Paint()    // 画笔

    // 地图区块，可用于绘制地图
    val rect = Rect()

    override fun drawComposite(canvas: Canvas) {
        val halfWidth = GameConfig.BLOCK_WIDTH * GameConfig.BLOCK_NUM_W / 2
        val halfHeight = GameConfig.BLOCK_WIDTH * GameConfig.BLOCK_NUM_H / 2

        with(paint) {
            color = Color.BLACK                  //设置画笔颜色
            style = Paint.Style.FILL             //设置填充样式
            strokeWidth = 20F                    //设置画笔宽度
            isAntiAlias = true
        }

        canvas.run {
            // 初始化画布并设置画布背景
            drawColor(Color.WHITE)

            // 刷新对象的Rect，用于动态碰撞检测
            rect.left = width / 2 - halfWidth
            rect.right = width / 2 + halfWidth
            rect.top = height / 2 - halfHeight
            rect.bottom = height / 2 + halfHeight

            drawRect(rect, paint)
        }
    }

    override fun refreshCompositeData() {
        // 比较对象两两之间的互动
        checkCollide()
    }

    // 比较对象两两之间的互动
    private val chain: ColliderChain = ColliderChain()
    private fun checkCollide() {
        for (i in 0 until children.size)
            for (j in 0 until children.size) run {
                val o1: Node = children[i] as Node
                val o2: Node = children[j] as Node
                chain.collide(o1, o2)
            }
    }
}