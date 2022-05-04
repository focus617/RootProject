package com.focus617.app_demo.engine.d2

import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.Vector4
import com.focus617.core.engine.renderer.Texture2D
import com.focus617.core.engine.renderer.XRenderer
import com.focus617.core.engine.scene.Camera
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher
import com.focus617.core.platform.event.base.EventType
import com.focus617.core.platform.event.screenTouchEvents.TouchDragEvent

class GameLayer(name: String, private val scene: XGLScene2D, val is3D: Boolean) : Layer(name) {
    private val eventDispatcher = EventDispatcher()

    override fun onAttach() {
        LOG.info("${this.mDebugName} onAttach()")
        registerEventHandlers()
    }

    override fun onDetach() {
        LOG.info("${this.mDebugName} onDetach")
        unRegisterEventHandlers()
    }

    override fun onUpdate(timeStep: TimeStep) {
        if (!Renderer2DData.initialized || !scene.initialized) return

        beginScene(scene.mCamera)
        Renderer2DData.drawQuad(Vector2(-0.8f, -1.0f), Vector2(0.5f, 0.8f), RED)
        Renderer2DData.drawQuad(Vector2(0.5f, 0.5f), Vector2(0.75f, 0.5f), BLUE)
//        Renderer2DData.drawRotatedQuad(Vector2(0.5f, -0.5f), Vector2(0.5f, 0.75f), 45f, BLUE)
//
        Renderer2DData.drawQuad(
            Vector3(-1.5f, -1.5f, -0.1f),
            Vector2(2f, 2f),
            Renderer2DData.TextureSlots[XGLScene2D.textureIndex] as Texture2D,
            10f
        )

        endScene()
    }

    fun beginScene(camera: Camera) {
        // 在多线程渲染里，会把BeginScene函数放在RenderCommandQueue里执行
        // camera在多线程渲染的时候不能保证主线程是否正在更改Camera的相关信息
        synchronized(camera) {
            System.arraycopy(camera.getProjectionMatrix(), 0, XRenderer.sProjectionMatrix, 0, 16)
            System.arraycopy(camera.getViewMatrix(), 0, XRenderer.sViewMatrix, 0, 16)
        }

        with(Renderer2DData) {
            QuadVertexBufferPtr = 0
            QuadIndexCount = 0
            TextureSlotIndex = 1
        }
    }

    fun endScene() {}

    override fun onEvent(event: Event): Boolean {
        return eventDispatcher.dispatch(event)
    }

    override fun close() {
        LOG.info("${this.mDebugName} closed")
        eventDispatcher.close()
    }

    // 处理各种触屏事件，例如可能引起相机位置变化的事件
    private fun registerEventHandlers() {
        eventDispatcher.register(EventType.TouchDrag) { event ->
            val e: TouchDragEvent = event as TouchDragEvent
            LOG.info("${this.mDebugName}: ${e.name} from ${e.source} received")
//                LOG.info("It's type is ${e.eventType}")
//                LOG.info("It's was submit at ${DateHelper.timeStampAsStr(e.timestamp)}")
//                LOG.info("Current position is (${e.x}, ${e.y})\n")
            val hasConsumed = scene.mCameraController.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.TouchPress) { event ->
            val hasConsumed = scene.mCameraController.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.PinchStart) { event ->
            val hasConsumed = scene.mCameraController.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.PinchEnd) { event ->
            val hasConsumed = scene.mCameraController.onEvent(event)
            hasConsumed
        }

        eventDispatcher.register(EventType.Pinch) { event ->
            val hasConsumed = scene.mCameraController.onEvent(event)
            hasConsumed
        }
    }

    private fun unRegisterEventHandlers() {
        eventDispatcher.unRegister(EventType.TouchDrag)
        eventDispatcher.unRegister(EventType.TouchPress)
        eventDispatcher.unRegister(EventType.PinchStart)
        eventDispatcher.unRegister(EventType.PinchEnd)
        eventDispatcher.unRegister(EventType.Pinch)
    }

    companion object {
        val WHITE = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
        val RED = Vector4(0.8f, 0.3f, 0.2f, 1.0f)
        val BLUE = Vector4(0.2f, 0.3f, 0.8f, 1.0f)

    }
}
