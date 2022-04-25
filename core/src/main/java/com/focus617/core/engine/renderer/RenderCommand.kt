package com.focus617.core.engine.renderer

import com.focus617.core.engine.baseDataType.Color
import com.focus617.core.platform.base.BaseEntity

object RenderCommand: BaseEntity() {
    var sRendererAPI: RendererAPI? = null

    fun init(){
        sRendererAPI?.init()
    }

    fun clear(){
        sRendererAPI?.clear()
    }

    fun setClearColor(color: Color){
        sRendererAPI?.setClearColor(color)
    }

    fun drawIndexed(vertexArray: VertexArray){
        sRendererAPI?.drawIndexed(vertexArray)
    }

}

