package com.focus617.core.engine.scene_graph

import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.platform.event.base.Event

interface IfComponent {
    var mParent: GameEntity

    fun onEvent(event: Event)
    fun onUpdate(transform: Transform)
    fun onRender(shader: Shader, transform: Transform)

    fun getTransform() = mParent.getTransform()
}