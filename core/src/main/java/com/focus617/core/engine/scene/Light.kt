package com.focus617.core.engine.scene

import com.focus617.core.engine.math.Vector3

class Light {

    companion object{
        const val U_VECTOR_TO_LIGHT = "u_VectorToLight"
        const val U_POINT_LIGHT_POSITIONS = "u_PointLightPositions"
        const val U_POINT_LIGHT_COLORS = "u_PointLightColors"

        const val U_MODEL_MATRIX = "u_ModelMatrix"

        val vectorToLight = Vector3(0.61f, 0.64f, -0.47f).normalize()
    }
}