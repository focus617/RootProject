package com.focus617.core.engine.objects.d3

import com.focus617.core.engine.objects.DynamicCreationObject
import com.focus617.core.engine.objects.ObjectBuilder
import kotlin.math.PI

class Cylinder(var radius: Float, var height: Float) : DynamicCreationObject() {

    override fun beforeBuild() {
        val numPoints: Int = (2 * PI * radius * 10).toInt()
        ObjectBuilder.appendOpenCylinder(radius, height, numPoints)
        ObjectBuilder.appendCircle(radius, numPoints, -height/2)
        ObjectBuilder.appendCircle(radius, numPoints, height/2)
        buildData = ObjectBuilder.buildData()
    }

}