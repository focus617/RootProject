package com.focus617.core.engine.mesh.d3

import com.focus617.core.engine.mesh.GeomMeshBuilder
import com.focus617.core.engine.scene_graph.renderer.DynamicCreationMesh

// TODO: Z轴显示不正常
class Star(
    var angleNum: Int,  // 星形的锐角个数
    var radius: Float,  // 内角半径
    var R: Float,       // 外角半径
    var z: Float,       // z轴基准坐标
) : DynamicCreationMesh() {

    override fun beforeBuild() {
        GeomMeshBuilder.appendStar(angleNum, radius, R, z)
        buildData = GeomMeshBuilder.buildMeshData(false)
    }

}