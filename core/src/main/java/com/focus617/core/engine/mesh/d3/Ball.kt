package com.focus617.core.engine.mesh.d3

import com.focus617.core.engine.mesh.DynamicCreationMesh
import com.focus617.core.engine.mesh.GeomMeshBuilder

open class Ball(var radius: Float) : DynamicCreationMesh() {

    override fun beforeBuild() {
        GeomMeshBuilder.appendBall(radius)
        buildData = GeomMeshBuilder.buildMeshData()
    }

}