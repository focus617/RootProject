package com.focus617.tankwar.scene

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import com.focus617.platform.config_util.PropertiesUtil
import com.focus617.platform.helper.BitmapHelper.bitmapLoader
import com.focus617.tankwar.R
import com.focus617.tankwar.scene.base.*
import com.focus617.tankwar.scene.components.Bullet
import com.focus617.tankwar.scene.components.Explode
import com.focus617.tankwar.scene.components.tank.Tank
import java.util.*

object GameConfig {
    var BLOCK_WIDTH: Int = 0        // 游戏方格的宽度
    var BLOCK_NUM_W: Int = 0        // 游戏场地横向方格的个数
    var BLOCK_NUM_H: Int = 0        // 游戏场地纵向方格的个数
}

class GameScene(val context: Context) : IfScene, IfRefresh {

    // 被绘制的对象集合
    override val rootNode = RootNode("Scene")
    private lateinit var aggregateStatic: AggregateNode
    private lateinit var aggregateTank: AggregateNode
    private lateinit var aggregateBullet: AggregateNode

    // 绘制对象所用的Bitmap仓库
    override val bitmapRepository: LinkedHashMap<String, Bitmap> = LinkedHashMap()

    private val resource = context.resources
    val properties: Properties? = PropertiesUtil.loadProperties(context)

    init {
        loadGameConfig()
        loadGameResource()
        initAggregateNode()
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

    // 加载游戏资源，例如构造绘制对象的Bitmap仓库
    fun loadGameResource() {
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

    // 初始化场景中的集合对象
    private fun initAggregateNode(){
        aggregateStatic = AggregateNode(GameConstant.STATIC_AGGREGATE_NODE)
        aggregateTank = AggregateNode(GameConstant.TANK_AGGREGATE_NODE)
        aggregateBullet = AggregateNode(GameConstant.BULLET_AGGREGATE_NODE)
        with(rootNode){
            add(aggregateStatic)
            add(aggregateTank)
            add(aggregateBullet)
        }
    }

    // 初始化场景中的对象(必须在initAggregateNode()之后调用)
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
            aggregateTank.add(
                Tank(
                    "Tank", this, isEnemy,
                    random.nextInt(GameConfig.BLOCK_NUM_W),
                    random.nextInt(GameConfig.BLOCK_NUM_H),
                    Dir.values()[random.nextInt(Dir.values().size)]
                )
            )
        }
    }

    fun getTanks(): List<Tank> = aggregateTank.getChildren() as List<Tank>

    fun removeTank(tank: Tank){
        aggregateTank.remove(tank)
    }

    fun addBullet(xPos: Int, yPos: Int, dir: Dir) {
        aggregateBullet.add(Bullet("bullet", this, xPos, yPos, dir))
    }

    fun removeBullet(bullet: Bullet){
        aggregateBullet.remove(bullet)
    }

    fun addExplode(xPos: Int, yPos: Int) {
        aggregateStatic.add(Explode("explode", this, xPos, yPos))
    }

    override fun draw(canvas: Canvas) = rootNode.draw(canvas)
    override fun refreshData() = rootNode.refreshData()


}