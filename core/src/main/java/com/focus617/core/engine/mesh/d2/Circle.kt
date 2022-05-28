package com.focus617.core.engine.mesh.d2

import com.focus617.core.engine.mesh.DynamicCreationMesh
import com.focus617.core.engine.mesh.GeomMeshBuilder
import kotlin.math.PI

class Circle(var radius: Float) : DynamicCreationMesh() {

    override fun beforeBuild() {
        val numPoints: Int = (2 * PI * radius * 10).toInt()
        GeomMeshBuilder.appendCircle(radius, numPoints)
        buildData = GeomMeshBuilder.buildMeshData()
    }

}