package com.focus617.core.ecs.mine.component

import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector3

data class Camera(
    val position: Point3D = Point3D(0.0f, 0.0f, 0.0f),
    val target: Point3D = Point3D(0.0f, 1.0f, 0.0f),

    val directionUp: Vector3 = Vector3(0.0f, 1.0f, 0.0f),
    val directionFront: Vector3 = Vector3(0.0f, 0.0f, -1.0f),
    val directionRight: Vector3 = Vector3(1.0f, 0.0f, 0.0f),

    var mWidth: Int = 0,            // Viewport size
    var mHeight: Int = 0,
    var mZoomLevel: Float = 0.5f
)


