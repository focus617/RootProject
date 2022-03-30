package com.focus617.tankwar.di.scene

import android.content.Context
import android.graphics.*
import com.focus617.tankwar.R
import com.focus617.tankwar.scene.base.IDraw
import com.focus617.tankwar.scene.base.RootNode
import com.focus617.tankwar.scene.components.ClockPin
import com.focus617.tankwar.scene.components.Tank
import com.focus617.tankwar.scene.components.XRect

class MyTestScene(context: Context) : IDraw {

    private val rootNode = RootNode("Scene")

    init {
        with(rootNode) {
            add(ClockPin("clock"))
            add(XRect("BlueRect"))
            add(Tank("Tank", context))
        }
    }

    override fun draw(canvas: Canvas) = rootNode.draw(canvas)
}