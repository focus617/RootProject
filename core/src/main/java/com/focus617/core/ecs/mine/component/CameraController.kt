package com.focus617.core.ecs.mine.component

import com.focus617.core.engine.math.Mat4

data class CameraController(
    val projectionMatrix: Mat4 = Mat4()
)
