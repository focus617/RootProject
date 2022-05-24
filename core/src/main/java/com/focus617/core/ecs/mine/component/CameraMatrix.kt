package com.focus617.core.ecs.mine.component

import com.focus617.core.engine.math.Mat4

data class CameraMatrix(
    val projectionMatrix: Mat4 = Mat4(),
    val viewMatrix: Mat4 = Mat4()
)
