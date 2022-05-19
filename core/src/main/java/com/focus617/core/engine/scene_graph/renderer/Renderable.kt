package com.focus617.core.engine.scene_graph.renderer

import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.platform.base.BaseEntity

abstract class Renderable: BaseEntity() {
    val mesh = ArrayList<Mesh>()
    val material = HashMap<String, Material>()
    val shader = HashMap<String, Shader>()
}