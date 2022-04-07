package com.focus617.tankwar.scene

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.focus617.tankwar.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class GameSceneTest {

    private lateinit var scene: GameScene
    private lateinit var context: Context

    @Before
    fun setUp() {
        // Context of the app under test.
        context = InstrumentationRegistry.getInstrumentation().targetContext
        scene = GameScene.TerrainBuilder(context)
            .loadGameConfig()
            .loadGameResource()     // 加载游戏资源
            .buildNodes()           // 初始化场景中的对象
            .build()
    }

    @Test
    fun initBitmap_bitmapCanLoadCorrect() {
        scene.bitmapRepository.clear()
        assertEquals(0, scene.bitmapRepository.size)

        val resource: Resources = context.resources
        scene.bitmapRepository["Tank_Good_Up"] =
            BitmapFactory.decodeResource(resource, R.drawable.ic_tank_good_up)

        assertEquals(1, scene.bitmapRepository.size)
        assertNotNull(scene.bitmapRepository["Tank_Good_Up"])
    }

    @Test
    fun loadBitmap_LoadCorrect() {
        // Then
        assertEquals(20, scene.bitmapRepository.size)
    }

}
