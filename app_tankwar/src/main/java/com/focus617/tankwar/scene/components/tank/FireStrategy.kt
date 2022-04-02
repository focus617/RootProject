package com.focus617.tankwar.scene.components.tank

interface FireStrategy {

    fun fire(tank: Tank)    // 传入tank参数的目的是为了得到炮弹的位置

}