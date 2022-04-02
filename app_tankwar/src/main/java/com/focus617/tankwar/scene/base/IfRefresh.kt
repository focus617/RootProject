package com.focus617.tankwar.scene.base

import android.graphics.Canvas

interface IfRefresh {

    fun draw(canvas: Canvas)            // 刷新图像

    fun refreshData()     // 刷新数据

}