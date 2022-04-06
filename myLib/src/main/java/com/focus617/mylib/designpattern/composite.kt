package com.focus617.mylib.designpattern

import com.focus617.mylib.designpattern.platform.BaseObject
import com.focus617.mylib.logging.WithLogging

/**
 * @description 为组合对象声明接口，抽象所有类成员共有的默认行为
 */
abstract class ComponentBase(val name: String) : BaseObject() {
    /**
     * 添加部件
     */
    abstract fun add(component: ComponentBase)

    /**
     * 移除部件
     */
    abstract fun remove(component: ComponentBase)

    /**
     * 展示当前部件
     */
    abstract fun display(depth: Int)
}

/**
 * @description 叶子节点对象，叶子节点没有子节点
 */
class Leaf(name: String) : ComponentBase(name) {
    override fun add(component: ComponentBase) {
        LOG.info("cannot add from a leaf")
    }

    override fun remove(component: ComponentBase) {
        LOG.info("cannot remove from a leaf")
    }

    override fun display(depth: Int) {
        LOG.info(Array(depth) { "-" }.joinToString(" ") + name)
    }

}

/**
 * @description 有枝节点对象，用来存储子部件
 */
class Composite(name: String) : ComponentBase(name) {

    //孩子
    private val children = arrayListOf<ComponentBase>()

    override fun add(component: ComponentBase) {
        children.add(component)
    }

    override fun remove(component: ComponentBase) {
        children.remove(component)
    }

    override fun display(depth: Int) {
        LOG.info(Array(depth) { "-" }.joinToString(" ") + name)//输出本身
        for (child in children) {//遍历其孩子
            child.display(depth + 2)
        }
    }

}

// 测试类
class ClientComposite {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg args: String) {
            //构建根节点
            val root = Composite("root")
            root.add(Leaf("Leaf A"))
            root.add(Leaf("Leaf B"))

            //根上长出分支X，分支上长出了两片叶子
            val compositeX = Composite("Composite X")
            compositeX.add(Leaf("Leaf XA"))
            compositeX.add(Leaf("Leaf XB"))
            root.add(compositeX)

            //分支X再长出分支XY
            val compositeXY = Composite("Composite XY")
            compositeXY.add(Leaf("Leaf XYA"))
            compositeXY.add(Leaf("Leaf XYB"))
            compositeX.add(compositeXY)

            //根部又长出叶子
            root.add(Leaf("Leaf C"))

            val leaf = Leaf("Leaf D")
            root.add(leaf)
            root.remove(leaf)

            //显示大树的样子
            root.display(1)

        }
    }
}