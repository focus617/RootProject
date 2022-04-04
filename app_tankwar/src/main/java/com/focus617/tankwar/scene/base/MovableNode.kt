package com.focus617.tankwar.scene.base

/*
 * MovableNode类的主要职责是负责管理可移动对象的移动方向，速度，变向，以及被消灭等规则
 */
abstract class MovableNode(scene: IfScene) : Node(scene), IfMovable {

    //移动方向
    abstract override var dir: Dir

    //移动速度
    abstract override var speed: Int

    var previousX: Int = 0
    var previousY: Int = 0

    override fun refreshData() {
        move()
        super.refreshData()
    }

    override fun move() {
        previousX = x
        previousY = y

        when (dir) {
            Dir.UP -> y -= speed
            Dir.DOWN -> y += speed
            Dir.LEFT -> x -= speed
            Dir.RIGHT -> x += speed
        }
    }

}