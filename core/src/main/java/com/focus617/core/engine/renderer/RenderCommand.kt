package com.focus617.core.engine.renderer

import com.focus617.core.engine.resource.baseDataType.Color
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

    fun setViewport(x: Int, y: Int, width: Int, height: Int){
        sRendererAPI?.setViewport(x, y, width, height)
    }

    fun drawIndexed(vertexArray: VertexArray, indexCount: Int = 0){
        sRendererAPI?.drawIndexed(vertexArray, indexCount)
    }

}

