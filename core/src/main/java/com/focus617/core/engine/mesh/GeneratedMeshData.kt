package com.focus617.core.engine.mesh

import com.focus617.core.engine.renderer.vertex.BufferLayout

class GeneratedMeshData (
    val numVertices: Int,        // 顶点数量
    val vertices: FloatArray,    // 顶点数组
    val layout: BufferLayout,    // 顶点数组的布局
    val indices: ShortArray      // 顶点索引数组
)