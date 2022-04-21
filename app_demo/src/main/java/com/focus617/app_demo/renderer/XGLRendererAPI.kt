package com.focus617.app_demo.renderer

import android.opengl.GLES31
import com.focus617.core.engine.baseDataType.Color
import com.focus617.core.engine.renderer.RendererAPI
import com.focus617.core.engine.renderer.VertexArray

class XGLRendererAPI : RendererAPI() {
    override fun clear() {
        // 清理屏幕，重绘背景颜色
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT or GLES31.GL_DEPTH_BUFFER_BIT)
    }

    override fun setClearColor(color: Color) {
        GLES31.glClearColor(color.r, color.g, color.b, color.a)
    }

    override fun drawIndexed(vertexArray: VertexArray) {
        // 图元装配，绘制三角形
        GLES31.glDrawElements(
            GLES31.GL_TRIANGLES,
            (vertexArray as XGLVertexArray).getIndexBuffer()!!.mCount,
            GLES31.GL_UNSIGNED_SHORT, 0
        )
    }
}