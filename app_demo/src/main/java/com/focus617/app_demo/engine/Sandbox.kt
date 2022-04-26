package com.focus617.app_demo.engine

import android.content.Context
import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventType
import com.focus617.core.platform.event.base.LayerEventDispatcher
import com.focus617.core.platform.event.screenTouchEvents.TouchDragEvent
import com.focus617.core.platform.event.screenTouchEvents.TouchPressEvent
import com.focus617.mylib.helper.DateHelper

class Sandbox(context: Context) : Engine() {
    init {
        pushLayer(ExampleLayer("ExampleLayer"))
        pushOverLayer(ExampleLayer("ExampleOverlay"))
    }

    inner class ExampleLayer(name: String) : Layer(name) {
        private val eventDispatcher = LayerEventDispatcher()

        init {
            testRegisterEventHandlers()
        }

        override fun onAttach() {
            LOG.info("${this.mDebugName} onAttach()")
            testRegisterEventHandlers()
        }

        override fun onDetach() {
            LOG.info("${this.mDebugName} onDetach")
        }

        override fun onUpdate(timeStep: TimeStep) {
            mWindow?.mRenderer?.apply {
                // Update Camera
                mCameraController.onUpdate(timeStep)

                // Render UI
                mWindow!!.onUpdate()
            }

        }

        override fun onEvent(event: Event): Boolean {
            LOG.info("${this.mDebugName} onEvent $event")
            return eventDispatcher.dispatch(event)
        }

        private fun testRegisterEventHandlers() {
            eventDispatcher.register(EventType.TouchDrag) { event ->
                val e: TouchDragEvent = event as TouchDragEvent
                LOG.info("${this.mDebugName}: ${e.name} from ${e.source} received")
                LOG.info("It's type is ${e.eventType}")
                LOG.info("It's was submit at ${DateHelper.timeStampAsStr(e.timestamp)}")
                LOG.info("Current position is (${e.x}, ${e.y})")
                event.handleFinished()
                true
            }

            eventDispatcher.register(EventType.TouchPress) { event ->
                val e: TouchPressEvent = event as TouchPressEvent
                LOG.info("${this.mDebugName}: ${e.name} from ${e.source} received")
                LOG.info("It's type is ${e.eventType}")
                LOG.info("It's was submit at ${DateHelper.timeStampAsStr(e.timestamp)}")
                LOG.info("Current position is (${e.x}, ${e.y})")
                event.handleFinished()
                true
            }
        }

    }
}