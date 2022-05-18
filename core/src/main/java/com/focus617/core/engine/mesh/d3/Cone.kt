package com.focus617.core.engine.mesh.d3

import com.focus617.core.engine.mesh.GeomMeshBuilder
import com.focus617.core.engine.scene_graph.renderer.DynamicCreationMesh
import kotlin.math.PI

class Cone(var radius: Float, var height: Float) : DynamicCreationMesh() {

    override fun beforeBuild() {
        val numPoints: Int = (2 * PI * radius * 10).toInt()
        GeomMeshBuilder.appendCone(radius, height, numPoints)
        GeomMeshBuilder.appendCircle(radius, numPoints, 0f)
        buildData = GeomMeshBuilder.buildMeshData()
    }

}