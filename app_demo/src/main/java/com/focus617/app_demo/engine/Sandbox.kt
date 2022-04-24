package com.focus617.app_demo.engine

import android.content.Context
import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventType
import com.focus617.core.platform.event.base.LayerEventDispatcher
import com.focus617.core.platform.event.screenTouchEvents.TouchMovedEvent
import com.focus617.mylib.helper.DateHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Sandbox(context: Context) : Engine() {
    init {
        pushLayer(ExampleLayer("ExampleLayer"))
        pushOverLayer(ExampleLayer("ExampleOverlay"))
    }

    inner class ExampleLayer(name: String) : Layer(name) {
        private val eventDispatcher = LayerEventDispatcher()

        override fun onAttach() {
            LOG.info("${this.mDebugName} onAttach()")
            testRegisterEventHandlers()
        }

        override fun onDetach() {
            LOG.info("${this.mDebugName} onDetach")
        }

        private var mCameraRotation: Float = 90F
        private var mCameraRotationSpeed: Float = 0.001F
        override fun onUpdate(timeStep: TimeStep) {
            mCameraRotation += timeStep.getMilliSecond() * mCameraRotationSpeed

            // 清理屏幕，重绘背景颜色
            //RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1F))
            //RenderCommand.clear()

            mWindow?.mRenderer?.apply {
                //mCamera.setPosition(0.5F, 0.5F, 0F)
                mCamera.setRotation(mCameraRotation)

                beginScene(mCamera)

                // Render UI
                mWindow!!.onUpdate()

                endScene()
            }

        }

        override fun onEvent(event: Event): Boolean {
            LOG.info("${this.mDebugName} onEvent $event")

            var result: Boolean = false
            CoroutineScope(Dispatchers.Default).launch {
                result = eventDispatcher.dispatch(event)
            }
            return result
        }

        private fun testRegisterEventHandlers() {
            eventDispatcher.register(EventType.TouchMoved) { event ->
                val e: TouchMovedEvent = event as TouchMovedEvent
                LOG.info("${e.name} from ${e.source} received")
                LOG.info("It's type is ${e.eventType}")
                LOG.info("It's was submit at ${DateHelper.timeStampAsStr(e.timestamp)}")
                LOG.info("Current position is (${e.x}, ${e.y})")
                event.handleFinished()
                false
            }
        }

    }
}