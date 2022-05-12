package com.focus617.core.engine.objects

import com.focus617.core.engine.renderer.vertex.BufferLayout

abstract class DynamicCreationObject() : DrawableObject() {
    var buildData: GeneratedData? = null

    // beforeBuild function must be overridden by subclass

    override fun afterBuild() {
        buildData = null
    }

    override fun getVertices(): FloatArray {
        if (buildData == null)
            LOG.error("You should call beforeBuild at first.")
        return buildData!!.vertices
    }

    override fun getLayout(): BufferLayout {
        if (buildData == null)
            LOG.error("You should call beforeBuild at first.")
        return buildData!!.layout
    }

    override fun getIndices(): ShortArray {
        if (buildData == null)
            LOG.error("You should call beforeBuild at first.")
        return buildData!!.indices
    }
}