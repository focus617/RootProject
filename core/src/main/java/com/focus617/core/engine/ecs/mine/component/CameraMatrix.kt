package com.focus617.core.engine.ecs.mine.component

import com.focus617.core.engine.math.Mat4

data class CameraMatrix(
    val projectionMatrix: Mat4 = Mat4(),
    val viewMatrix: Mat4 = Mat4()
)

data class PerspectiveCamera(val mark: Int = 0)

data class OrthographicCamera(val mark: Int = 0)


