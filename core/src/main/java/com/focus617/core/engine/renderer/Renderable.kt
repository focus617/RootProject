package com.focus617.core.engine.renderer

import com.focus617.core.engine.mesh.Mesh
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.platform.base.BaseEntity
import java.io.Closeable

abstract class Renderable: BaseEntity(), Closeable {
    //模型所包含的Mesh集合
    val mMeshes = HashMap<String, Mesh>()

    //模型所包含的Material集合
    val mMaterials = HashMap<String, Material>()

    //模型所包含的Shader集合
    val shader = HashMap<String, Shader>()


    override fun close() {
        mMeshes.clear()
        mMaterials.clear()
    }
}