package com.focus617.core.engine.scene

import com.focus617.core.engine.math.Vector3

data class Material(
    var ambient: Vector3,
    var diffuse: Vector3,
    var specular: Vector3,  // 镜面强度(Specular Intensity)
    var shininess: Float,   // 高光的反光度

    val name: String,

    var textureDiffuse: Int = 0,
    var textureSpecular: Int = 0
)
