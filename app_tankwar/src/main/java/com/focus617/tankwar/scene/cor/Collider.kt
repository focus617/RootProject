package com.focus617.tankwar.scene.cor

import com.focus617.tankwar.scene.base.Node

interface Collider {
    // 返回true，可以继续比较；
    // 如果是false，对象已被销毁，也就没有必要继续比较下去了
    fun collide(node1: Node, node2: Node): Boolean

}