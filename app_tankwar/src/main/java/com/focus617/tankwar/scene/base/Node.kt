package com.focus617.tankwar.scene.base

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import com.focus617.platform.helper.BitmapHelper.draw
import com.focus617.tankwar.scene.GameConfig
import com.focus617.tankwar.scene.GameScene

/*
 * Node类的主要职责是负责在SurfaceView上绘制Bitmap
 */
abstract class Node(name: String, val scene: IfScene) : Leaf(name) {

    //在游戏棋盘上的坐标
    open var x: Int = (scene as GameScene).rootNode.rect.left
    open var y: Int = (scene as GameScene).rootNode.rect.top

    // 每个对象在地图上占用的区块，可用于碰撞检测
    val rect = Rect()

    lateinit var bitmap: Bitmap

    // 被绘制对象是否仍有效（未出边界，未被摧毁）？
    protected var isAlive: Boolean = true

    open fun die() {
        this.isAlive = false
    }


    override fun draw(canvas: Canvas) {
        // 找到自己此时合适的Bitmap
        findBitmap()

        // 刷新对象的Rect，用于绘制和动态碰撞检测
        with(rect) {
            left = x
            right = x + GameConfig.BLOCK_WIDTH
            top = y
            bottom = y + GameConfig.BLOCK_WIDTH
        }

        // 在绘制以前，坐标(x,y)将由具体实现类进行计算更新
        bitmap.draw(canvas, rect)

    }

    override fun refreshData() {
        // 执行策略检查
        checkStrategy()
    }

    // 实现子类需要负责在Scene中的BitmapRepository找到自己当时合适的Bitmap
    // 本函数将在每次draw之前被调用，以便根据移动方向，变换相应的Bitmap
    abstract fun findBitmap()

    // 实现子类需要负责实现自己的各项策略，比如移动到边界，或碰到障碍后的处理操作
    abstract fun checkStrategy()

}