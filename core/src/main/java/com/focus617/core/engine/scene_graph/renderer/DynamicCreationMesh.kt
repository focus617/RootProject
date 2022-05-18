package com.focus617.core.engine.scene_graph.renderer

import com.focus617.core.engine.mesh.GeneratedMeshData
import com.focus617.core.engine.renderer.vertex.BufferLayout
import com.focus617.core.engine.scene_graph.IfMeshable
import com.focus617.core.platform.base.BaseEntity

abstract class DynamicCreationMesh : BaseEntity(), IfMeshable {
    var buildData: GeneratedMeshData? = null

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