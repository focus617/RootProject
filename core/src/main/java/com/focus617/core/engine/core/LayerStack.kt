package com.focus617.core.engine.core

import com.focus617.core.platform.base.BaseEntity

class LayerStack : BaseEntity() {
    val mLayers = mutableListOf<Layer>()

    operator fun iterator(): Iterator<Layer> {
        return mLayers.iterator()
    }

    fun begin() = mLayers.first()
    fun end() = mLayers.last()

    fun PushLayer(layer: Layer) {
        mLayers.add(layer)
        layer.onAttach()
    }

    fun PopLayer(layer: Layer) {
        mLayers.remove(layer)
        layer.onDetach()
    }

}