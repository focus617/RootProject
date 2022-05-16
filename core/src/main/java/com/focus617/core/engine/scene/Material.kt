package com.focus617.core.engine.scene

import com.focus617.core.engine.math.Vector3

class Material(val name: String) {
    var ambient: Vector3 = Vector3(0.763f, 0.657f, 0.614f)
    var diffuse: Vector3 = Vector3(0.763f, 0.657f, 0.614f)

    // 镜面强度(Specular Intensity)
    var specular: Vector3 = Vector3(0.5f, 0.5f, 0.5f)
    var shininess: Float = 100.0f  // 高光的反光度

    var textureDiffuse: Int = 0
    var textureSpecular: Int = 0
}
