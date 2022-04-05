package com.focus617.tankwar.scene.base

interface IfMovable {

    //移动方向
    var dir: Dir

    //移动速度
    var speed: Int

    var previousX: Int
    var previousY: Int

    fun moveForward()

    fun back()

}