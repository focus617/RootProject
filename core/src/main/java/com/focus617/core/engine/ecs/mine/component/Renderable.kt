package com.focus617.core.engine.ecs.mine.component

data class Renderable(
    var shader: Int = -1,                       // Shader Handle
    var mesh: Int = -1,                         // VertexArray Handle
    var textures: Array<Int> = emptyArray()    // Array of Texture Handle
)
