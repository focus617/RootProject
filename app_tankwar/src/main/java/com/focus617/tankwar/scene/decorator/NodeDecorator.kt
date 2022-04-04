package com.focus617.tankwar.scene.decorator

import com.focus617.mylib.designpattern.Component
import com.focus617.tankwar.scene.base.IfScene
import com.focus617.tankwar.scene.base.Node

abstract class NodeDecorator(scene: IfScene): Node(scene) {

    // 被装饰者
    private lateinit var node: Node

    // 装扮方法
//    fun decorate(component: Component) {
//        this.component = component
//    }

}