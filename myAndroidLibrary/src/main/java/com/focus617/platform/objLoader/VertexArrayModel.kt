package com.focus617.platform.objLoader

import com.focus617.core.engine.renderer.vertex.BufferLayout

data class VertexArrayModel(
    val textureName: String,
    val vertices: FloatArray,    // 顶点数组
    val layout: BufferLayout,    // 顶点数组的布局
    val indices: ShortArray      // 顶点索引数组
) {
    override fun equals(other: Any?): Boolean {
        return if (other !is VertexArrayModel) false
        else (textureName == other.textureName
                && vertices === other.vertices
                && layout == other.layout
                && indices === other.indices)
    }

    override fun hashCode(): Int {
        val BASE = 17
        val MULTIPLIER = 31
        var result = BASE

        result = MULTIPLIER * result + textureName.length
        result = MULTIPLIER * result + vertices.size
        result = MULTIPLIER * result + layout.getStride()
        result = MULTIPLIER * result + indices.size
        return result
    }
}