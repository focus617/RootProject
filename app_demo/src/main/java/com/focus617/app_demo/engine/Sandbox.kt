package com.focus617.app_demo.engine

import android.content.Context
import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher
import com.focus617.core.platform.event.base.EventType
import com.focus617.core.platform.event.screenTouchEvents.*

class Sandbox(context: Context) : Engine() {
    init {
        pushLayer(ExampleLayer("ExampleLayer"))
        pushOverLayer(ExampleLayer("ExampleOverlay"))
    }

    inner class ExampleLayer(name: String) : Layer(name) {
        private val eventDispatcher = EventDispatcher()

        init {
            registerEventHandlers()
        }

        override fun onAttach() {
            LOG.info("${this.mDebugName} onAttach()")
            registerEventHandlers()
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
            return eventDispatcher.dispatch(event)
        }

        private fun registerEventHandlers() {

            eventDispatcher.register(EventType.TouchDrag) { event ->
                val e: TouchDragEvent = event as TouchDragEvent
//                LOG.info("${this.mDebugName}: ${e.name} from ${e.source} received")
//                LOG.info("It's type is ${e.eventType}")
//                LOG.info("It's was submit at ${DateHelper.timeStampAsStr(e.timestamp)}")
//                LOG.info("Current position is (${e.x}, ${e.y})\n")
                val hasConsumed =  mWindow?.mRenderer?.mCameraController?.onEvent(event) ?: false
                hasConsumed
            }

            eventDispatcher.register(EventType.TouchPress) { event ->
                val e: TouchPressEvent = event as TouchPressEvent
                val hasConsumed =  mWindow?.mRenderer?.mCameraController?.onEvent(event) ?: false
                hasConsumed
            }

            eventDispatcher.register(EventType.PinchStart) { event ->
                val e: PinchStartEvent = event as PinchStartEvent
                val hasConsumed =  mWindow?.mRenderer?.mCameraController?.onEvent(event) ?: false
                hasConsumed
            }

            eventDispatcher.register(EventType.PinchEnd) { event ->
                val e: PinchEndEvent = event as PinchEndEvent
                val hasConsumed =  mWindow?.mRenderer?.mCameraController?.onEvent(event) ?: false
                hasConsumed
            }

            eventDispatcher.register(EventType.Pinch) { event ->
                val e: PinchEvent = event as PinchEvent
                val hasConsumed =  mWindow?.mRenderer?.mCameraController?.onEvent(event) ?: false
                hasConsumed
            }
        }

    }
}