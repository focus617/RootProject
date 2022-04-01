package com.focus617.tankwar.scene

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import com.focus617.platform.helper.BitmapHelper.bitmapLoader
import com.focus617.platform.helper.BitmapHelper.rotate
import com.focus617.tankwar.R
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.IfDraw
import com.focus617.tankwar.scene.base.IfScene
import com.focus617.tankwar.scene.base.RootNode
import com.focus617.tankwar.scene.components.Bullet
import com.focus617.tankwar.scene.components.Explode
import com.focus617.tankwar.scene.components.Tank

class GameScene(val context: Context) : IfScene, IfDraw {

    // 被绘制的对象集合
    override val rootNode = RootNode("Scene")

    // 绘制对象所用的Bitmap仓库
    override val bitmapRepository: LinkedHashMap<String, Bitmap> = LinkedHashMap()

    private val resource = context.resources

    init {
        loadBitmap()
        initNodes()
    }

    // 初始化场景中的对象
    private fun initNodes() {
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

    // 构造绘制对象的Bitmap仓库
    fun loadBitmap() {
        loadTankBitmap()
        loadBulletBitmap()
        loadExplodesBitmap()
    }

    private fun loadTankBitmap() {
        val bitmap = bitmapLoader(resource, R.drawable.ic_tank_good_up)
        bitmapRepository[GameConstant.TANK_GOOD_UP] = bitmap
        bitmapRepository[GameConstant.TANK_GOOD_RIGHT] = bitmap.rotate(90F)
        bitmapRepository[GameConstant.TANK_GOOD_DOWN] = bitmap.rotate(180F)
        bitmapRepository[GameConstant.TANK_GOOD_LEFT] = bitmap.rotate(270F)
    }

    private fun loadBulletBitmap() {
        val bitmap = bitmapLoader(resource, R.drawable.ic_bullet_up)
        bitmapRepository[GameConstant.BULLET_UP] = bitmap
        bitmapRepository[GameConstant.BULLET_RIGHT] = bitmap.rotate(90F)
        bitmapRepository[GameConstant.BULLET_DOWN] = bitmap.rotate(180F)
        bitmapRepository[GameConstant.BULLET_LEFT] = bitmap.rotate(270F)
    }

    private fun loadExplodesBitmap() {
        bitmapRepository[GameConstant.EXPLODE_1] = bitmapLoader(resource, R.drawable.e1)
        bitmapRepository[GameConstant.EXPLODE_2] = bitmapLoader(resource, R.drawable.e2)
        bitmapRepository[GameConstant.EXPLODE_3] = bitmapLoader(resource, R.drawable.e3)
        bitmapRepository[GameConstant.EXPLODE_4] = bitmapLoader(resource, R.drawable.e4)
        bitmapRepository[GameConstant.EXPLODE_5] = bitmapLoader(resource, R.drawable.e5)
        bitmapRepository[GameConstant.EXPLODE_6] = bitmapLoader(resource, R.drawable.e6)
        bitmapRepository[GameConstant.EXPLODE_7] = bitmapLoader(resource, R.drawable.e7)
        bitmapRepository[GameConstant.EXPLODE_8] = bitmapLoader(resource, R.drawable.e8)
        bitmapRepository[GameConstant.EXPLODE_9] = bitmapLoader(resource, R.drawable.e9)
        bitmapRepository[GameConstant.EXPLODE_10] = bitmapLoader(resource, R.drawable.e10)
        bitmapRepository[GameConstant.EXPLODE_11] = bitmapLoader(resource, R.drawable.e11)
        bitmapRepository[GameConstant.EXPLODE_12] = bitmapLoader(resource, R.drawable.e12)
        bitmapRepository[GameConstant.EXPLODE_13] = bitmapLoader(resource, R.drawable.e13)
        bitmapRepository[GameConstant.EXPLODE_14] = bitmapLoader(resource, R.drawable.e14)
        bitmapRepository[GameConstant.EXPLODE_15] = bitmapLoader(resource, R.drawable.e15)
        bitmapRepository[GameConstant.EXPLODE_16] = bitmapLoader(resource, R.drawable.e16)
    }

    override fun draw(canvas: Canvas) = rootNode.draw(canvas)

    fun addBullet(xPos: Int, yPos: Int, dir: Dir) {
        rootNode.add(Bullet("bullet", context, this, xPos, yPos, dir))
    }

    fun addExplode(xPos: Int, yPos: Int) {
        rootNode.add(Explode("explode", context, this, xPos, yPos))
    }
}