package com.focus617.tankwar.scene

object GameConfig {
    const val BLOCK_WIDTH = 140      // 游戏方格的宽度
    const val BLOCK_NUM_W = 10        // 游戏场地横向方格的个数
    const val BLOCK_NUM_H = 17        // 游戏场地纵向方格的个数

    const val TANK_SPEED = BLOCK_WIDTH /10   // 坦克的移动速度
    const val BULLET_SPEED = BLOCK_WIDTH /5  // 炮弹的移动速度
}

object GameConstant {
    const val TANK_GOOD_UP = "Tank_Good_Up"
    const val TANK_GOOD_DOWN = "Tank_Good_Down"
    const val TANK_GOOD_LEFT = "Tank_Good_Left"
    const val TANK_GOOD_RIGHT = "Tank_Good_Right"

    const val BULLET_UP = "Bullet_Up"
    const val BULLET_DOWN = "Bullet_Down"
    const val BULLET_LEFT = "Bullet_Left"
    const val BULLET_RIGHT = "Bullet_Right"
}