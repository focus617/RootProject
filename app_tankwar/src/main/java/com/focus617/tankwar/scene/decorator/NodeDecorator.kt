package com.focus617.tankwar.scene.decorator

import android.graphics.Canvas
import com.focus617.tankwar.scene.base.IfRefresh
import com.focus617.tankwar.scene.base.Node

abstract class NodeDecorator(node: Node) : IfRefresh {

    // 被装饰者
    var node: Node = node

    // 算法方法
    override fun draw(canvas: Canvas) {
        node.draw(canvas)
    }

    override fun refreshData() {
        node.refreshData()
    }
}