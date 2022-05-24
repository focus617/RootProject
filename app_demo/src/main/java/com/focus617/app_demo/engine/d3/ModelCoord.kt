package com.focus617.app_demo.engine.d3

import android.content.Context
import com.focus617.core.engine.scene_graph.GeometryEntity
import com.focus617.opengles.scene_graph.ModelRenderable
import com.focus617.opengles.scene_graph.XGLDrawableObject

class ModelCoord: GeometryEntity(), XGLDrawableObject {
    init {
        shaderName = XGL3DResourceManager.CommonShader
    }


    override fun initOpenGlResource() {
        setRenderable(model)
    }

    companion object{
        lateinit var model: ModelRenderable
        fun initModel(context: Context){
            model = ModelRenderable.builder(context, "3d/coordinate3D/position_gizmo.obj")
        }
    }

}