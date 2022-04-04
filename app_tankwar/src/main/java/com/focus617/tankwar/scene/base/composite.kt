package com.focus617.tankwar.scene.base

import android.graphics.Canvas
import com.focus617.mylib.logging.WithLogging
import com.focus617.mylib.logging.unwrapCompanionClass
import timber.log.Timber

/**
 * @description 组合对象实现IDraw接口，抽象所有类成员共有的默认行为
 */

/**
 * @description 叶子节点对象，叶子节点没有子节点
 */
abstract class Leaf(var name: String) : IfRefresh {
    // 提供 Logger
    companion object : WithLogging()

    abstract override fun draw(canvas: Canvas)

    abstract override fun refreshData()
}

/**
 * @description 有枝节点对象，用来存储子部件
 */
abstract class Composite(var name: String) : IfRefresh {
    // 提供 Logger
    companion object : WithLogging()

    protected val children = arrayListOf<IfRefresh>()
    fun getChildren() = children.toList()

    // 添加部件
    open fun add(component: IfRefresh) {
        children.add(component)
        Timber.d("add ${unwrapCompanionClass(component.javaClass).simpleName}\n" +
                "--now total ${children.size} in list")
    }

    // 移除部件
    open fun remove(component: IfRefresh) {
        children.remove(component)
        Timber.d("remove ${unwrapCompanionClass(component.javaClass).simpleName}\n" +
                "--now total ${children.size} in list")
    }

    abstract fun drawComposite(canvas: Canvas)

    override fun draw(canvas: Canvas) {
        this.drawComposite(canvas)

        for (child in children) {//遍历其孩子
            child.draw(canvas)
        }
    }

    abstract fun refreshCompositeData()

    override fun refreshData(){
        this.refreshCompositeData()

        for (child in children) {//遍历其孩子
            child.refreshData()
        }
    }

}

