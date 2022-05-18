package com.focus617.core.engine.scene_graph.renderer

object ShaderUniformConstants {
    const val U_MODEL_MATRIX = "u_ModelMatrix"
    const val U_VIEW_MATRIX = "u_ViewMatrix"
    const val U_PROJECT_MATRIX = "u_ProjectionMatrix"

    const val U_POINT_VIEW_POSITION = "u_ViewPos"
    const val U_POINT_LIGHT_POSITION = "light.position"
    const val U_POINT_LIGHT_AMBIENT = "light.ambient"
    const val U_POINT_LIGHT_DIFFUSE = "light.diffuse"
    const val U_POINT_LIGHT_SPECULAR = "light.specular"
    const val U_POINT_LIGHT_CONSTANT = "light.constant"
    const val U_POINT_LIGHT_LINEAR = "light.linear"
    const val U_POINT_LIGHT_QUADRATIC = "light.quadratic"

    const val U_MATERIAL_TEXTURE_DIFFUSE = "material.diffuse"
    const val U_MATERIAL_SPECULAR = "material.specular"
    const val U_MATERIAL_SHININESS = "material.shininess"
}