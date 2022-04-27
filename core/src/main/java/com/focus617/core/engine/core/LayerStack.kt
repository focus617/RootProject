package com.focus617.core.engine.core

import com.focus617.core.platform.base.BaseEntity
import java.io.Closeable

class LayerStack : BaseEntity(), Closeable {
    val mLayers = mutableListOf<Layer>()

    override fun close() {
        mLayers.forEach {
            it.onDetach()
            it.close()
        }
    }

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