package com.focus617.tankwar.scene.decorator

import android.graphics.Canvas
import com.focus617.tankwar.scene.base.IfRefresh
import com.focus617.tankwar.scene.base.Node

abstract class NodeDecorator(
    var node: Node      // 被装饰者
) : IfRefresh {

    // 本对象是否仍有效（未出边界，未被摧毁）？
    var isAlive: Boolean = true

    open fun die() {
        this.isAlive = false
    }

    // 算法方法
    override fun draw(canvas: Canvas) {
        node.draw(canvas)
    }

    override fun refreshData() {
        node.refreshData()

        if(!(node.isAlive))
            die()
    }
}