package com.focus617.core.engine.scene

import com.focus617.core.engine.math.Vector3

class Light {

    companion object {
        const val U_VECTOR_TO_LIGHT = "u_VectorToLight"
        const val U_POINT_LIGHT_POSITIONS = "u_PointLightPositions"
        const val U_POINT_LIGHT_COLORS = "u_PointLightColors"

        val vectorToLight = Vector3(0.61f, 0.64f, -0.47f).normalize()
    }
}

// 点光源
object PointLight {
    var position: Vector3 = Vector3(6.1f, 6.4f, -4.7f)

    var ambient: Vector3 = Vector3(0.3f, 0.3f, 0.3f)
    var diffuse: Vector3 = Vector3(1.0f, 1.0f, 1.0f)
    var specular: Vector3 = Vector3(1.0f, 1.0f, 1.0f)

    const val Constant: Float = 1.0f
    const val Linear: Float = 0.09f
    const val Quadratic: Float = 0.032f
}

// 聚光
object SpotLight {
    var position: Vector3 = Vector3(3.0f, 4.0f, -6.0f)
    var direction: Vector3 = Vector3(-3.0f, -4.0f, 6.0f)
    var cutOff: Float = 12.5f

    var ambient: Vector3 = Vector3(0.2f, 0.2f, 0.2f)
    var diffuse: Vector3 = Vector3(1.0f, 1.0f, 1.0f)
    var specular: Vector3 = Vector3(1.0f, 1.0f, 1.0f)

    const val Constant: Float = 1.0f
    const val Linear: Float = 0.09f
    const val Quadratic: Float = 0.032f
}

// 平行光
object DirectionalLight {
    var direction: Vector3 = Vector3(-3.0f, -4.0f, 6.0f)

    var ambient: Vector3 = Vector3(0.2f, 0.2f, 0.2f)
    var diffuse: Vector3 = Vector3(1.0f, 1.0f, 1.0f)
    var specular: Vector3 = Vector3(1.0f, 1.0f, 1.0f)
}