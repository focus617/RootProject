package com.focus617.core.engine.objects.d3

import com.focus617.core.engine.objects.DynamicCreationObject
import com.focus617.core.engine.objects.ObjectBuilder

open class Ball(var radius: Float) : DynamicCreationObject() {

    override fun beforeBuild() {
        ObjectBuilder.appendBall(radius)
        buildData = ObjectBuilder.buildData()
    }

}