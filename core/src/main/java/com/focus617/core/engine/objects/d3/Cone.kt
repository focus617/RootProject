package com.focus617.core.engine.objects.d3

import com.focus617.core.engine.objects.DynamicCreationObject
import com.focus617.core.engine.objects.ObjectBuilder
import kotlin.math.PI

class Cone(var radius: Float, var height: Float) : DynamicCreationObject() {

    override fun beforeBuild() {
        val numPoints: Int = (2 * PI * radius * 10).toInt()
        ObjectBuilder.appendCone(radius, height, numPoints)
        ObjectBuilder.appendCircle(radius, numPoints, 0f)
        buildData = ObjectBuilder.buildData()
    }

}