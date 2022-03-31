package com.focus617.tankwar.scene

import android.content.Context
import android.graphics.Canvas
import com.focus617.tankwar.scene.base.IDraw
import com.focus617.tankwar.scene.base.RootNode

class GameScene(context: Context) : IDraw {

    private val resource = context.resources
    private val rootNode = RootNode("Scene")

    init {
        with(rootNode) {

        }

    }

    override fun draw(canvas: Canvas) = rootNode.draw(canvas)

}