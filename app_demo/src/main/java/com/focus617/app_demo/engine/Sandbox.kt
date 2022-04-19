package com.focus617.app_demo.engine

import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.core.Layer
import com.focus617.core.platform.event.base.Event

class Sandbox : Engine() {
    init{
        pushLayer(ExampleLayer("ExampleLayer"))
        pushOverLayer(ExampleLayer("ExampleOverlay"))
    }

    inner class ExampleLayer(name: String): Layer(name){
        override fun onAttach() {
            LOG.info("${this.mDebugName} onAttach()")
        }

        override fun onDetach() {
            LOG.info("${this.mDebugName} onDetach")
        }

        override fun onUpdate() {
        }

        override fun onEvent(event: Event): Boolean {
            LOG.info("${this.mDebugName} onEvent $event")
            return true
        }

    }
}