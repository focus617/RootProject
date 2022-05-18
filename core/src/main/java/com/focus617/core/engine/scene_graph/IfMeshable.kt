package com.focus617.core.engine.scene_graph

import com.focus617.core.engine.renderer.vertex.BufferLayout

interface IfMeshable {
    fun beforeBuild()
    fun afterBuild()
    fun getVertices(): FloatArray
    fun getLayout(): BufferLayout
    fun getIndices(): ShortArray
}