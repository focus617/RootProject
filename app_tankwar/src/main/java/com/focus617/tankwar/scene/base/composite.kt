package com.focus617.tankwar.scene.base

import android.graphics.Canvas
import timber.log.Timber

/**
 * @description 组合对象实现IDraw接口，抽象所有类成员共有的默认行为
 */

/**
 * @description 叶子节点对象，叶子节点没有子节点
 */
abstract class Leaf(var name: String) : IfDraw {

    abstract override fun draw(canvas: Canvas)

}

/**
 * @description 有枝节点对象，用来存储子部件
 */
abstract class Composite(var name: String) : IfDraw {

    private val children = arrayListOf<IfDraw>()

    // 添加部件
    open fun add(component: IfDraw) {
        children.add(component)
        Timber.d("add--${children.size} in list")
    }

    // 移除部件
    open fun remove(component: IfDraw) {
        children.remove(component)
        Timber.d("remove--${children.size} in list")
    }

    abstract fun drawComposite(canvas: Canvas)

    override fun draw(canvas: Canvas) {
        this.drawComposite(canvas)

        for (child in children) {//遍历其孩子
            child.draw(canvas)
        }
    }

}

