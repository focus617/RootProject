package com.focus617.core.engine.scene_graph.components

import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.scene_graph.core.IfComponent
import com.focus617.core.engine.scene_graph.core.ParentEntity
import com.focus617.core.engine.scene_graph.core.Transform
import com.focus617.core.engine.scene_graph.renderer.Material
import com.focus617.core.engine.scene_graph.renderer.Mesh
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_MODEL_MATRIX
import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event

class MeshRenderer(mesh: Mesh, material: Material?) : BaseEntity(), IfComponent {
    override lateinit var mParent: ParentEntity
    private var mMesh: Mesh = mesh
    private var mMaterial: Material? = material

    override fun close() {}

    override fun onEvent(event: Event): Boolean = false

    override fun onUpdate(timeStep: TimeStep, transform: Transform?) {}

    override fun onRender(shader: Shader, transform: Transform?) {
        transform?.apply {
            shader.bind()

            // 设置材质相关的Texture和参数
            mMaterial?.onRender(shader)

            // TODO: Model Matrix need to be global matrix
//        LOG.info(transform.getLocalModelMatrix().toString("ModelMatrix"))
            shader.setMat4(U_MODEL_MATRIX, transform.getLocalModelMatrix())
            mMesh.draw()
            shader.unbind()
        }
    }
}