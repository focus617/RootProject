package com.focus617.core.engine.scene_graph

import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Vector3

/**
 * SPACE INFORMATION
 */
data class Transform (
    //Local space information
    val pos: Vector3 = Vector3(0.0f, 0.0f, 0.0f),
    val eulerRot: Vector3 = Vector3(0.0f, 0.0f, 0.0f),
    val scale: Vector3 = Vector3( 1.0f, 1.0f, 1.0f),

    //Global space information concatenate in matrix
    val modelMatrix: Mat4 = Mat4()
)