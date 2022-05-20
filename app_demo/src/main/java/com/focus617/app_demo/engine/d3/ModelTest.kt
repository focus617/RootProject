package com.focus617.app_demo.engine.d3

import android.content.Context
import com.focus617.app_demo.engine.XGLDrawableObject
import com.focus617.app_demo.scene_graph.ModelRenderable
import com.focus617.core.engine.scene_graph.GeometryEntity

class ModelTest: GeometryEntity(), XGLDrawableObject {
    init {
        shaderName = XGLScene3D.CommonShaderFilePath
    }


    override fun initOpenGlResource() {
        setRenderable(model)
    }

    companion object{
        lateinit var model: ModelRenderable
        fun initModel(context: Context){
            model = ModelRenderable.builder(context, "3d/Andy/andy.obj")
        }
    }

}