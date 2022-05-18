package com.focus617.core.engine.mesh.d3

import com.focus617.core.engine.mesh.GeomMeshBuilder
import com.focus617.core.engine.scene_graph.renderer.DynamicCreationMesh
import kotlin.math.PI

class Cylinder(var radius: Float, var height: Float) : DynamicCreationMesh() {

    override fun beforeBuild() {
        val numPoints: Int = (2 * PI * radius * 10).toInt()
        GeomMeshBuilder.appendOpenCylinder(radius, height, numPoints)
        GeomMeshBuilder.appendCircle(radius, numPoints, -height/2)
        GeomMeshBuilder.appendCircle(radius, numPoints, height/2)
        buildData = GeomMeshBuilder.buildMeshData()
    }

}