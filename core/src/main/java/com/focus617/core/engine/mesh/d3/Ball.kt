package com.focus617.core.engine.mesh.d3

import com.focus617.core.engine.mesh.GeomMeshBuilder
import com.focus617.core.engine.scene_graph.renderer.DynamicCreationMesh

open class Ball(var radius: Float) : DynamicCreationMesh() {

    override fun beforeBuild() {
        GeomMeshBuilder.appendBall(radius)
        buildData = GeomMeshBuilder.buildMeshData()
    }

}