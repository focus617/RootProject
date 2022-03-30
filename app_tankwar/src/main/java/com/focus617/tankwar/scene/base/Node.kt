package com.focus617.tankwar.scene.base

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

abstract class Node(name: String, context: Context) :Leaf(name){

    protected val resource = context.resources
    protected lateinit var bitmap: Bitmap

    // bitmap的参考坐标
    protected var x: Int = 0
    protected var y: Int = 0

    protected val paint = Paint()    // 画笔

    abstract override fun draw(canvas: Canvas)

}