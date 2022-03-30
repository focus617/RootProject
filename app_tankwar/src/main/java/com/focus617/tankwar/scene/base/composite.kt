package com.focus617.tankwar.scene.base

import android.graphics.Canvas

/**
 * @description 组合对象实现IDraw接口，抽象所有类成员共有的默认行为
 */

/**
 * @description 叶子节点对象，叶子节点没有子节点
 */
abstract class Node(var name: String) : IDraw {

    abstract override fun draw(canvas: Canvas)

}

/**
 * @description 有枝节点对象，用来存储子部件
 */
abstract class Composite(var name: String) : IDraw {

    private val children = arrayListOf<IDraw>()

    // 添加部件
    open fun add(component: IDraw) {
        children.add(component)
    }

    // 移除部件
    open fun remove(component: IDraw) {
        children.remove(component)
    }

    abstract fun drawComposite(canvas: Canvas)

    override fun draw(canvas: Canvas) {
        drawComposite(canvas)
        for (child in children) {//遍历其孩子
            child.draw(canvas)
        }
    }

}

