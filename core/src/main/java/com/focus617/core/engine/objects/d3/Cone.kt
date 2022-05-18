package com.focus617.core.engine.objects.d3

import com.focus617.core.engine.objects.ObjectBuilder
import com.focus617.core.engine.scene_graph.renderer.DynamicCreationMesh
import kotlin.math.PI

class Cone(var radius: Float, var height: Float) : DynamicCreationMesh() {

    override fun beforeBuild() {
        val numPoints: Int = (2 * PI * radius * 10).toInt()
        ObjectBuilder.appendCone(radius, height, numPoints)
        ObjectBuilder.appendCircle(radius, numPoints, 0f)
        buildData = ObjectBuilder.buildData()
    }

}