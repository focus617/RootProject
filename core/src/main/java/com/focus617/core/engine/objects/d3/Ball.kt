package com.focus617.core.engine.objects.d3

import com.focus617.core.engine.objects.ObjectBuilder
import com.focus617.core.engine.scene_graph.renderer.DynamicCreationMesh

open class Ball(var radius: Float) : DynamicCreationMesh() {

    override fun beforeBuild() {
        ObjectBuilder.appendBall(radius)
        buildData = ObjectBuilder.buildData()
    }

}