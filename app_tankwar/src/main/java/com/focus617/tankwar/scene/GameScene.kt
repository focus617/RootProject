package com.focus617.tankwar.scene

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
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
        loadBitmap(context)

        rootNode.add(Tank("myTank", context, this))
        rootNode.add(
            Tank(
                "enemyTank", context, this,
                GameConfig.BLOCK_NUM_W / 2, GameConfig.BLOCK_NUM_H - 1, Dir.UP
            )
        )

    }

    override fun draw(canvas: Canvas) = rootNode.draw(canvas)

    fun loadBitmap(context: Context) {
        loadTankBitmap(context)
        loadBulletBitmap(context)
    }

    private fun loadTankBitmap(context: Context) {
        val resource: Resources = context.resources

        val tankBitmap = BitmapFactory.decodeResource(resource, R.drawable.ic_tank_good_up)
        bitmapRepository[GameConstant.TANK_GOOD_UP] = tankBitmap

        var matrix = Matrix()
        //设置旋转角度
        matrix.setRotate(90F, (tankBitmap.width / 2).toFloat(), (tankBitmap.height / 2).toFloat())
        //通过待旋转的图片和角度生成新的图片
        var newTankBitmap = Bitmap.createBitmap(
            tankBitmap, 0, 0,
            tankBitmap.width, tankBitmap.height, matrix, true
        )
        bitmapRepository[GameConstant.TANK_GOOD_RIGHT] = newTankBitmap

        matrix = Matrix()
        //设置旋转角度
        matrix.setRotate(180F, (tankBitmap.width / 2).toFloat(), (tankBitmap.height / 2).toFloat())
        //通过待旋转的图片和角度生成新的图片
        newTankBitmap = Bitmap.createBitmap(
            tankBitmap, 0, 0,
            tankBitmap.width, tankBitmap.height, matrix, true
        )
        bitmapRepository[GameConstant.TANK_GOOD_DOWN] = newTankBitmap

        matrix = Matrix()
        //设置旋转角度
        matrix.setRotate(270F, (tankBitmap.width / 2).toFloat(), (tankBitmap.height / 2).toFloat())
        //通过待旋转的图片和角度生成新的图片
        newTankBitmap = Bitmap.createBitmap(
            tankBitmap, 0, 0,
            tankBitmap.width, tankBitmap.height, matrix, true
        )
        bitmapRepository[GameConstant.TANK_GOOD_LEFT] = newTankBitmap
    }


    private fun loadBulletBitmap(context: Context) {
        val resource: Resources = context.resources

        val bulletBitmap = BitmapFactory.decodeResource(resource, R.drawable.ic_bullet_up)
        bitmapRepository[GameConstant.BULLET_UP] = bulletBitmap

        var matrix = Matrix()
        //设置旋转角度
        matrix.setRotate(90F, (bulletBitmap.width / 2).toFloat(), (bulletBitmap.height / 2).toFloat())
        //通过待旋转的图片和角度生成新的图片
        var newBulletBitmap = Bitmap.createBitmap(
            bulletBitmap, 0, 0,
            bulletBitmap.width, bulletBitmap.height, matrix, true
        )
        bitmapRepository[GameConstant.BULLET_RIGHT] = newBulletBitmap

        matrix = Matrix()
        //设置旋转角度
        matrix.setRotate(180F, (bulletBitmap.width / 2).toFloat(), (bulletBitmap.height / 2).toFloat())
        //通过待旋转的图片和角度生成新的图片
        newBulletBitmap = Bitmap.createBitmap(
            bulletBitmap, 0, 0,
            bulletBitmap.width, bulletBitmap.height, matrix, true
        )
        bitmapRepository[GameConstant.BULLET_DOWN] = newBulletBitmap

        matrix = Matrix()
        //设置旋转角度
        matrix.setRotate(270F, (bulletBitmap.width / 2).toFloat(), (bulletBitmap.height / 2).toFloat())
        //通过待旋转的图片和角度生成新的图片
        newBulletBitmap = Bitmap.createBitmap(
            bulletBitmap, 0, 0,
            bulletBitmap.width, bulletBitmap.height, matrix, true
        )
        bitmapRepository[GameConstant.BULLET_LEFT] = newBulletBitmap
    }

}