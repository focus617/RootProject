package com.focus617.app_demo.engine

import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.core.Layer
import com.focus617.core.platform.event.base.Event

class Sandbox(window: AndroidWindow) : Engine(window) {
    init{
        pushLayer(ExampleLayer)
    }

    companion object ExampleLayer: Layer("Example"){
        override fun onAttach() {
            LOG.info("ExampleLayer::onAttach()")
        }

        override fun onDetach() {
            LOG.info("ExampleLayer::onDetach")
        }

        override fun onUpdate() {
            LOG.info("ExampleLayer::onUpdate")
        }

        override fun onEvent(event: Event): Boolean {
            LOG.info("ExampleLayer::onEvent $event")
            return true
        }

    }
}