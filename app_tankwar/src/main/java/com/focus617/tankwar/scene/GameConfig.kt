package com.focus617.tankwar.scene

object GameConfig {
    const val BLOCK_WIDTH = 100      // 游戏方格的宽度
    const val BLOCK_NUM_W = 10        // 游戏场地横向方格的个数
    const val BLOCK_NUM_H = 19        // 游戏场地横向方格的个数

    const val BULLET_WIDTH = 30      // 炮弹的宽度

    const val TANK_SPEED = BLOCK_WIDTH /10   // 坦克的移动速度
    const val BULLET_SPEED = BLOCK_WIDTH /5  // 炮弹的移动速度
}