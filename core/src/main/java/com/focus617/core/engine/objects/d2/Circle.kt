package com.focus617.core.engine.objects.d2

import com.focus617.core.engine.objects.DynamicCreationObject
import com.focus617.core.engine.objects.ObjectBuilder
import kotlin.math.PI

class Circle(var radius: Float) : DynamicCreationObject() {

    override fun beforeBuild() {
        val numPoints: Int = (2 * PI * radius * 10).toInt()
        ObjectBuilder.appendCircle(radius, numPoints)
        buildData = ObjectBuilder.buildData()
    }

}