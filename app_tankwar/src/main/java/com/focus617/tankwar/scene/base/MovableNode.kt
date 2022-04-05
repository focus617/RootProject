package com.focus617.tankwar.scene.base

/*
 * MovableNode类的主要职责是负责管理可移动对象的移动方向，速度，变向，以及被消灭等规则
 */
abstract class MovableNode(scene: IfScene) : Node(scene), IfMovable {

    //移动方向
    abstract override var dir: Dir

    //移动速度
    abstract override var speed: Int

    override var previousX: Int = 0
    override var previousY: Int = 0

    override fun refreshData() {
        moveForward()
        super.refreshData()
    }

    override fun moveForward() {
        // 记录上一次的位置
        previousX = x
        previousY = y

        when (dir) {
            Dir.UP -> y -= speed
            Dir.DOWN -> y += speed
            Dir.LEFT -> x -= speed
            Dir.RIGHT -> x += speed
        }
    }

    // 退回移动之前,记录的上一次的位置
    override fun back(){
        x = previousX
        y = previousY
    }

}