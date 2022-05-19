package com.focus617.core.engine.scene_graph.core

import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.platform.event.base.Event

interface IfEntity {
    fun onEvent(event: Event)
    fun onUpdate(timeStep: TimeStep)
    fun onRender(shader: Shader)
}