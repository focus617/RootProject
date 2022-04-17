package com.focus617.core.engine.core

class LayerStack {
    val mLayers = mutableListOf<Layer>()

    operator fun iterator(): Iterator<Layer> {
        return mLayers.iterator()
    }

    fun begin() = mLayers.first()
    fun end() = mLayers.last()

    fun PushLayer(layer: Layer) {
        mLayers.add(layer)
    }

    fun PopLayer(layer: Layer) {
        mLayers.remove(layer)
    }

//    fun PushOverlay(overlay: Layer) {}
//    fun PopOverlay(overlay: Layer) {}

}