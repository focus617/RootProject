package com.focus617.tankwar.scene.base

import android.graphics.*
import com.focus617.platform.helper.BitmapHelper.draw
import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.GameScene
import java.io.Serializable

/*
 * Node类的主要职责是负责在SurfaceView上绘制Bitmap
 */
abstract class Node(val scene: IfScene) : Leaf(), Serializable {

    //在游戏棋盘上的坐标
    abstract var x: Int
    abstract var y: Int

    // 每个对象在地图上占用的区块，可用于碰撞检测
    val rect = Rect()

    lateinit var bitmap: Bitmap

    // 被绘制对象是否仍有效（未出边界，未被摧毁）？
    var isAlive: Boolean = true

    open fun die() {
        this.isAlive = false
    }


    override fun draw(canvas: Canvas) {
        // 找到自己此时合适的Bitmap,主要是应用于炸弹这类需要动画的场景
        findBitmap()

        // 刷新对象的Rect，用于绘制和动态碰撞检测
        with(rect) {
            val xBias = scene.rootNode.rect.left
            val yBias = scene.rootNode.rect.top

            left = xBias + x
            right = xBias + x + GameConfig.BLOCK_WIDTH
            top = yBias + y
            bottom = yBias + y + GameConfig.BLOCK_WIDTH
        }

        // 在绘制以前，坐标(x,y)将由具体实现类进行计算更新
        bitmap.draw(canvas, rect)

        drawMask(canvas)
    }

    private fun drawMask(canvas: Canvas) {
        val paint = Paint()

        with(paint) {
            color = Color.RED           //设置画笔颜色
            style = Paint.Style.STROKE    //设置填充样式
            strokeWidth = 1F            //设置画笔宽度
            isAntiAlias = true
        }
        canvas.drawLine(
            rect.left.toFloat(), rect.top.toFloat(),
            rect.right.toFloat(), rect.top.toFloat(), paint
        )
        canvas.drawLine(
            rect.left.toFloat(), rect.bottom.toFloat(),
            rect.right.toFloat(), rect.bottom.toFloat(), paint
        )
    }

    override fun refreshData() {
        // 执行策略检查
        checkStrategy()
    }

    // 实现子类需要负责在Scene中的BitmapRepository找到自己当时合适的Bitmap
    // 本函数将在每次draw之前被调用，以便根据移动方向，变换相应的Bitmap
    abstract fun findBitmap()

    // 实现子类需要扩展自己的各项策略，比如移动到边界，或碰到障碍后的处理操作
    open fun checkStrategy() {
        // 检查和销毁无效对象
        if (!this.isAlive) {
            (scene as GameScene).removeObject(this)
        }
    }

}