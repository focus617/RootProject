package com.focus617.core.engine.scene_graph.renderer

import com.focus617.core.engine.renderer.vertex.BufferLayout

interface IfModelLoader {
    fun beforeBuild()
    fun afterBuild()
    fun getVertices(): FloatArray
    fun getLayout(): BufferLayout
    fun getIndices(): ShortArray
}