package com.focus617.core.ecs.mine.component

import com.focus617.core.engine.resource.baseDataType.Color

data class Sprite(
    var texturePath: String = "",
    var color: Color = Color.WHITE
)