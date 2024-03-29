package com.focus617.app_demo.engine.d3

import android.opengl.GLES31.*
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.mine.component.Sprite
import com.focus617.core.engine.ecs.mine.component.Transform
import com.focus617.core.engine.ecs.mine.static.Scene
import com.focus617.core.engine.math.Ray
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.resource.baseDataType.Color
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher
import com.focus617.core.platform.event.base.EventType
import com.focus617.core.platform.event.screenTouchEvents.TouchLongPressEvent
import com.focus617.opengles.scene_graph.XGLDrawableObject

class GamePlayerLayer(name: String) : Layer(name) {
    private val eventDispatcher = EventDispatcher()

    private val box1 = Box()
    private val box2 = Box()
    private val andy = ModelTest()
    private val coord = ModelCoord()

    private val ecsBox: Entity = Scene.createEntity("Box") {
        add<Transform>()
        add<Sprite> { color = Color.RED }
    }

    init {
        box1.onTransform3D(
            Vector3(-2.5f, 0.54f, -2.0f),
            Vector3(1.0f, 1.0f, 1.0f)
        )
        gameObjectList.add(box1)

        box2.onTransform3D(
            Vector3(2.5f, 0.54f, -1.5f),
            Vector3(1.0f, 1.0f, 1.0f)
        )
        gameObjectList.add(box2)

        andy.onTransform3D(
            Vector3(0.0f, 0.0f, 0.0f),
            Vector3(8f, 8f, 8f)
        )
        gameObjectList.add(andy)

        coord.onTransform3D(
            Vector3(1.5f, 1.0f, 0.5f),
            Vector3(0.35f, 0.35f, 0.35f)
        )
        gameObjectList.add(coord)

        val mapper = Scene.world().mapper<Transform>()
        LOG.info("ecsBox has Transform component? ${mapper.contains(ecsBox)}")

        // 测试获取Entity的Component，修改Component的属性值
        val comp = mapper.getOrNull(ecsBox)
        comp?.apply {
            this.position.x = 1.5f
            this.rotationInDegree.x = 50f
        }
    }

    override fun initOpenGlResource() {
        for (gameObject in gameObjectList) {
            (gameObject as XGLDrawableObject).initOpenGlResource()
        }
    }

    override fun close() {
        LOG.info("${this.mDebugName} closed")
        eventDispatcher.close()
    }

    override fun onAttach() {
        LOG.info("${this.mDebugName} onAttach()")
        registerEventHandlers()
    }

    override fun onDetach() {
        LOG.info("${this.mDebugName} onDetach")
        unRegisterEventHandlers()
    }

    override fun onUpdate(timeStep: TimeStep) {
        //LOG.info("${this.mDebugName} onUpdate")
    }

    override fun beforeDrawFrame() {
        //Enable Cull Back Face
        glEnable(GL_CULL_FACE)
    }

    override fun afterDrawFrame() {
        //Enable Cull Back Face
        glDisable(GL_CULL_FACE)
    }

    override fun onEvent(event: Event): Boolean {
        return eventDispatcher.dispatch(event)
    }

    // 处理各种触屏事件，例如可能引起相机位置变化的事件
    private fun registerEventHandlers() {

        eventDispatcher.register(EventType.TouchLongPress) { event ->
            val e: TouchLongPressEvent = event as TouchLongPressEvent
            LOG.info("Engine: ${e.name} from ${e.source} received")

            // 把被LongPress的正交空间中的点映射到三维空间的一条直线：直线的近端映射到
            // 投影矩阵中定义的视椎体的近平面，直线的远端映射到视椎体的远平面。
            val ray: Ray = Ray.convertNormalized2DPointToRay(
                e.invertedViewProjectionMat,
                e.normalizedX,
                e.normalizedY
            )

            for (gameObject in gameObjectList) {
                gameObject.intersects(ray)
            }
            e.handleFinished()
            true
        }
    }

    private fun unRegisterEventHandlers() {
        eventDispatcher.unRegister(EventType.TouchLongPress)
    }

}