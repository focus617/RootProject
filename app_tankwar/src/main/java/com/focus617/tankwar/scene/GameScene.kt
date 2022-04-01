package com.focus617.tankwar.scene

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import com.focus617.platform.helper.BitmapHelper.rotate
import com.focus617.tankwar.R
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.IfDraw
import com.focus617.tankwar.scene.base.IfScene
import com.focus617.tankwar.scene.base.RootNode
import com.focus617.tankwar.scene.components.Tank

class GameScene(context: Context) : IfScene, IfDraw {

    // 被绘制的对象集合
    override val rootNode = RootNode("Scene")

    // 绘制对象所用的Bitmap仓库
    override val bitmapRepository: LinkedHashMap<String, Bitmap> = LinkedHashMap()

    private val resource = context.resources

    init {
        loadBitmap()

        rootNode.add(
            Tank(
                "myTank", context, this,
                0, GameConfig.BLOCK_NUM_H / 2, Dir.RIGHT
            )
        )
        rootNode.add(
            Tank(
                "enemyTank1", context, this,
                GameConfig.BLOCK_NUM_W / 2, GameConfig.BLOCK_NUM_H - 1, Dir.UP
            )
        )
        rootNode.add(
            Tank(
                "enemyTank2", context, this,
                GameConfig.BLOCK_NUM_W / 4, GameConfig.BLOCK_NUM_H / 4, Dir.UP
            )
        )
        rootNode.add(
            Tank(
                "enemyTank3", context, this,
                GameConfig.BLOCK_NUM_W - 2, GameConfig.BLOCK_NUM_H / 4, Dir.DOWN
            )
        )
    }

    override fun draw(canvas: Canvas) = rootNode.draw(canvas)

    fun loadBitmap() {
        loadTankBitmap()
        loadBulletBitmap()
        loadExplodesBitmap()
    }

    private fun loadTankBitmap() {
        val bitmap = BitmapFactory.decodeResource(resource, R.drawable.ic_tank_good_up)
        bitmapRepository[GameConstant.TANK_GOOD_UP] = bitmap
        bitmapRepository[GameConstant.TANK_GOOD_RIGHT] = bitmap.rotate(90F)
        bitmapRepository[GameConstant.TANK_GOOD_DOWN] = bitmap.rotate(180F)
        bitmapRepository[GameConstant.TANK_GOOD_LEFT] = bitmap.rotate(270F)
    }

    private fun loadBulletBitmap() {
        val bitmap = BitmapFactory.decodeResource(resource, R.drawable.ic_bullet_up)
        bitmapRepository[GameConstant.BULLET_UP] = bitmap
        bitmapRepository[GameConstant.BULLET_RIGHT] = bitmap.rotate(90F)
        bitmapRepository[GameConstant.BULLET_DOWN] = bitmap.rotate(180F)
        bitmapRepository[GameConstant.BULLET_LEFT] = bitmap.rotate(270F)
    }

    private fun loadExplodesBitmap() {
        bitmapRepository[GameConstant.EXPLODE_1] =
            BitmapFactory.decodeResource(resource, R.drawable.e1)
        bitmapRepository[GameConstant.EXPLODE_2] =
            BitmapFactory.decodeResource(resource, R.drawable.e2)
        bitmapRepository[GameConstant.EXPLODE_3] =
            BitmapFactory.decodeResource(resource, R.drawable.e3)
        bitmapRepository[GameConstant.EXPLODE_4] =
            BitmapFactory.decodeResource(resource, R.drawable.e4)
        bitmapRepository[GameConstant.EXPLODE_5] =
            BitmapFactory.decodeResource(resource, R.drawable.e5)
        bitmapRepository[GameConstant.EXPLODE_6] =
            BitmapFactory.decodeResource(resource, R.drawable.e6)
        bitmapRepository[GameConstant.EXPLODE_7] =
            BitmapFactory.decodeResource(resource, R.drawable.e7)
        bitmapRepository[GameConstant.EXPLODE_8] =
            BitmapFactory.decodeResource(resource, R.drawable.e8)
        bitmapRepository[GameConstant.EXPLODE_9] =
            BitmapFactory.decodeResource(resource, R.drawable.e9)
        bitmapRepository[GameConstant.EXPLODE_10] =
            BitmapFactory.decodeResource(resource, R.drawable.e10)
        bitmapRepository[GameConstant.EXPLODE_11] =
            BitmapFactory.decodeResource(resource, R.drawable.e11)
        bitmapRepository[GameConstant.EXPLODE_12] =
            BitmapFactory.decodeResource(resource, R.drawable.e12)
        bitmapRepository[GameConstant.EXPLODE_13] =
            BitmapFactory.decodeResource(resource, R.drawable.e13)
        bitmapRepository[GameConstant.EXPLODE_14] =
            BitmapFactory.decodeResource(resource, R.drawable.e14)
        bitmapRepository[GameConstant.EXPLODE_15] =
            BitmapFactory.decodeResource(resource, R.drawable.e15)
        bitmapRepository[GameConstant.EXPLODE_16] =
            BitmapFactory.decodeResource(resource, R.drawable.e16)
    }
}