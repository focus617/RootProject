package com.focus617.tankwar.scene

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import com.focus617.platform.config_util.PropertiesUtil
import com.focus617.platform.helper.BitmapHelper.bitmapLoader
import com.focus617.tankwar.R
import com.focus617.tankwar.scene.base.Dir
import com.focus617.tankwar.scene.base.IfDraw
import com.focus617.tankwar.scene.base.IfScene
import com.focus617.tankwar.scene.base.RootNode
import com.focus617.tankwar.scene.components.Bullet
import com.focus617.tankwar.scene.components.Explode
import com.focus617.tankwar.scene.components.Tank
import java.util.*

class GameScene(val context: Context) : IfScene, IfDraw {

    // 被绘制的对象集合
    override val rootNode = RootNode("Scene")

    // 绘制对象所用的Bitmap仓库
    override val bitmapRepository: LinkedHashMap<String, Bitmap> = LinkedHashMap()

    private val resource = context.resources

    init {
        loadGameConfig()
        loadBitmap()
        initNodes()
    }

    // 从Properties读取游戏棋盘的配置，例如大小
    private fun loadGameConfig(){
        // 游戏方格的宽度
        GameConfig.BLOCK_WIDTH =
            PropertiesUtil.loadProperties(context)?.getProperty(GameConstant.KEY_BLOCK_WIDTH)?.toInt() ?: 10
        // 游戏场地横向方格的个数
        GameConfig.BLOCK_NUM_W =
            PropertiesUtil.loadProperties(context)?.getProperty(GameConstant.KEY_BLOCK_NUM_W)?.toInt() ?: 10
        // 游戏场地纵向方格的个数
        GameConfig.BLOCK_NUM_H =
            PropertiesUtil.loadProperties(context)?.getProperty(GameConstant.KEY_BLOCK_NUM_H)?.toInt() ?: 10
    }

    // 初始化场景中的对象
    private fun initNodes() {
        loadTankFromProperties(GameConstant.KEY_FRIEND)
        loadTankFromProperties(GameConstant.KEY_ENEMY)
    }

    private fun loadTankFromProperties(key: String) {
        val random = Random()

        val isEnemy: Boolean = when (key) {
            GameConstant.KEY_FRIEND -> false
            GameConstant.KEY_ENEMY -> true
            else -> return
        }

        val tankCount = PropertiesUtil
            .loadProperties(context)?.getProperty(key)?.toInt() ?: 4

        for (i in 1..tankCount) {
            rootNode.add(
                Tank(
                    "Tank", context, this, isEnemy,
                    random.nextInt(GameConfig.BLOCK_NUM_W),
                    random.nextInt(GameConfig.BLOCK_NUM_H),
                    Dir.values()[random.nextInt(Dir.values().size)]
                )
            )
        }
    }

    // 构造绘制对象的Bitmap仓库
    fun loadBitmap() {
        loadTankBitmap()
        loadBulletBitmap()
        loadExplodesBitmap()
    }

    private fun loadTankBitmap() {
        bitmapRepository[GameConstant.TANK_MINE] =
            bitmapLoader(resource, R.drawable.ic_tank_good_up)
        bitmapRepository[GameConstant.TANK_ENEMY_1] =
            bitmapLoader(resource, R.drawable.ic_tank_enemy_1_up)
        bitmapRepository[GameConstant.TANK_ENEMY_2] =
            bitmapLoader(resource, R.drawable.ic_tank_enemy_2_up)
    }

    private fun loadBulletBitmap() {
        bitmapRepository[GameConstant.BULLET] = bitmapLoader(resource, R.drawable.ic_bullet_up)
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