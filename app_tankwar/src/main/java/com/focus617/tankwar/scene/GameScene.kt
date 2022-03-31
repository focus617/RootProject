package com.focus617.tankwar.scene

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.focus617.tankwar.R
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.IfDraw
import com.focus617.tankwar.scene.base.IfScene
import com.focus617.tankwar.scene.base.RootNode
import com.focus617.tankwar.scene.components.Tank

class GameScene(context: Context) : IfScene, IfDraw {

    override val rootNode = RootNode("Scene")
    override val bitmapRepository: LinkedHashMap<String, Bitmap> = LinkedHashMap()

    private val resource = context.resources

    init {
        rootNode.add(Tank("myTank", context, this))
        rootNode.add(
            Tank(
                "enemyTank", context, this,
                GameConfig.BLOCK_NUM_W / 2, GameConfig.BLOCK_NUM_H - 1, Dir.UP
            )
        )

    }

    override fun draw(canvas: Canvas) = rootNode.draw(canvas)

    fun loadBitmap() {
        bitmapRepository["Tank_Good_Up"] =
            BitmapFactory.decodeResource(resource, R.drawable.ic_tank_good_up)
        bitmapRepository["Tank_Good_Down"] =
            BitmapFactory.decodeResource(resource, R.drawable.ic_tank_good_up)
        bitmapRepository["Tank_Good_Left"] =
            BitmapFactory.decodeResource(resource, R.drawable.ic_tank_good_up)
        bitmapRepository["Tank_Good_Right"] =
            BitmapFactory.decodeResource(resource, R.drawable.ic_tank_good_up)
    }

}