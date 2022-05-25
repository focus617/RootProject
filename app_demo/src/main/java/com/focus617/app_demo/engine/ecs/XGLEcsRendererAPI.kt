package com.focus617.app_demo.engine.ecs

import com.focus617.app_demo.engine.d2.Renderer2DData
import com.focus617.app_demo.engine.d2.XGLRenderer2D
import com.focus617.core.engine.ecs.mine.api.EcsRendererAPI
import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.resource.baseDataType.Color

class XGLEcsRendererAPI : EcsRendererAPI() {

    override fun DrawQuad(transform: Mat4, color: Color) {
        if (Renderer2DData.initialized)
            XGLRenderer2D.drawQuad(transform, color)
    }
}