package com.focus617.core.engine.scene_graph

import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.platform.event.base.Event
import java.io.Closeable

interface IfComponent: Closeable {
    var mParent: NodeEntity

    fun onEvent(event: Event): Boolean
    fun onUpdate(timeStep: TimeStep, transform: Transform)
    fun onRender(shader: Shader, transform: Transform)

    fun getTransform() = mParent.mTransform
}