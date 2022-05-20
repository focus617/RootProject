package com.focus617.core.engine.scene_graph.components

import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.scene_graph.core.IfComponent
import com.focus617.core.engine.scene_graph.core.ParentEntity
import com.focus617.core.engine.scene_graph.core.Transform
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_POINT_LIGHT_AMBIENT
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_POINT_LIGHT_CONSTANT
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_POINT_LIGHT_DIFFUSE
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_POINT_LIGHT_LINEAR
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_POINT_LIGHT_POSITION
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_POINT_LIGHT_QUADRATIC
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_POINT_LIGHT_SPECULAR
import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event

open class Light : BaseEntity(), IfComponent {
    override lateinit var mParent: ParentEntity

    override fun onRender(shader: Shader, transform: Transform?) {}

    override fun onEvent(event: Event): Boolean = false

    override fun onUpdate(timeStep: TimeStep, transform: Transform?) {}

    override fun close() {}

    companion object {
        val vectorToLight = Vector3(0.61f, 0.64f, -0.47f).normalize()
    }
}

// 点光源
class PointLight : Light() {
    var position: Vector3 = Vector3(6.1f, 6.4f, -4.7f)

    private var ambient: Vector3 = Vector3(0.3f, 0.3f, 0.3f)
    private var diffuse: Vector3 = Vector3(1.0f, 1.0f, 1.0f)
    private var specular: Vector3 = Vector3(1.0f, 1.0f, 1.0f)

    private val Constant: Float = 1.0f
    private val Linear: Float = 0.09f
    private val Quadratic: Float = 0.032f

    override fun onRender(shader: Shader, transform: Transform?) {
        shader.setFloat3(U_POINT_LIGHT_POSITION, position)
        shader.setFloat3(U_POINT_LIGHT_AMBIENT, ambient)
        shader.setFloat3(U_POINT_LIGHT_DIFFUSE, diffuse)
        shader.setFloat3(U_POINT_LIGHT_SPECULAR, specular)
        shader.setFloat(U_POINT_LIGHT_CONSTANT, Constant)
        shader.setFloat(U_POINT_LIGHT_LINEAR, Linear)
        shader.setFloat(U_POINT_LIGHT_QUADRATIC, Quadratic)
    }
}

// 聚光
object SpotLight : Light() {
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
object DirectionalLight : Light() {
    var direction: Vector3 = Vector3(-3.0f, -4.0f, 6.0f)

    var ambient: Vector3 = Vector3(0.2f, 0.2f, 0.2f)
    var diffuse: Vector3 = Vector3(1.0f, 1.0f, 1.0f)
    var specular: Vector3 = Vector3(1.0f, 1.0f, 1.0f)
}