package com.focus617.tankwar.scene.cor

import com.focus617.tankwar.scene.base.Node

interface Collider {
    /**
     * 碰撞处理的方法
     * 返回:
     * true，表示对象已被处理（销毁），也就没有必要再进行下一步处理
     * false，表示本次未完成处理，需要继续
     */
    fun collide(node1: Node, node2: Node): Boolean

}