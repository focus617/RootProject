package com.focus617.tankwar.scene.base

import com.focus617.tankwar.scene.GameConfig

/*
 * MovableNode类的主要职责是负责管理可移动对象的移动方向，速度，变向，以及被消灭等规则
 */
abstract class MovableNode(name: String, scene: IfScene) : Node(name, scene) {

    //移动方向
    abstract var dir: Dir

    //移动速度
    abstract var speed: Int

    override fun refreshData() {
        move()
        super.refreshData()
    }

    open fun move() {
        when (dir) {
            Dir.UP -> y -= speed
            Dir.DOWN -> y += speed
            Dir.LEFT -> x -= speed
            Dir.RIGHT -> x += speed
        }
    }

}