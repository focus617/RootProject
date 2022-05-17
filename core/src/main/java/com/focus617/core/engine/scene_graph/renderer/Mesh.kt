package com.focus617.core.engine.scene_graph.renderer

import com.focus617.core.engine.renderer.vertex.VertexArray

abstract class Mesh {
    // vertexArray is initialized via calling XGLVertexArray.buildVertexArray
    abstract val vertexArray: VertexArray

    abstract fun draw()
}