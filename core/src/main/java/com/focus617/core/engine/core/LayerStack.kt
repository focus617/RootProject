package com.focus617.core.engine.core

import java.util.*

class LayerStack {
    private val mLayers = Vector<Layer>()
    private val mLayerInsert = Vector<Layer>()::iterator

    fun begin() = mLayers.first()
    fun end() = mLayers.last()

    fun PushLayer(layer: Layer) {}
    fun PopLayer(layer: Layer) {}

    fun PushOverlay(overlay: Layer) {}
    fun PopOverlay(overlay: Layer) {}

}