package com.focus617.core.engine.scene_graph.components

import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.scene_graph.IfComponent
import com.focus617.core.engine.scene_graph.Transform
import com.focus617.core.engine.scene_graph.renderer.Material
import com.focus617.core.engine.scene_graph.renderer.Mesh
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_MODEL_MATRIX
import com.focus617.core.platform.event.base.Event

class MeshRenderer(mesh: Mesh, material: Material) : IfComponent {
    private var mMesh: Mesh = mesh
    private var mMaterial: Material = material

    override fun onEvent(event: Event) {}

    override fun onUpdate(transform: Transform) {}

    override fun onRender(shader: Shader, transform: Transform) {
        shader.bind()
        // TODO: Model Matrix need to be global matrix
        shader.setMat4(U_MODEL_MATRIX, transform.getLocalModelMatrix())
        mMesh.draw()

        shader.unbind()
    }
}