package com.focus617.core.engine.ecs.mine.component

import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector3

data class Transform(
    val position: Point3D = Point3D(0f, 0f, 0f),
    val rotationInDegree: Vector3 = Vector3(0f, 0f, 0f),
    val scale: Vector3 = Vector3(1.0f, 1.0f, 1.0f)
)

data class TransformMatrix(
    val transform: Mat4 = Mat4()
)

/**
 * An empty component to indicate the TransformMatrix need be refined.
 * So once somebody change the Transform component, he should add this component
 * to the entity who own the transform. Thus we can use family to sort all entity
 * with Dirty Component, then recalculate the matrix.
 * TODO: is there a way to sort entities by 'parent' first?
 */
data class Dirty(
    var dirty: Boolean = true
)
