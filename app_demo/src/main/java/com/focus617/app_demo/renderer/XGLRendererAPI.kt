package com.focus617.app_demo.renderer

import android.opengl.GLES20.*
import android.opengl.GLES31
import com.focus617.core.engine.baseDataType.Color
import com.focus617.core.engine.renderer.RendererAPI
import com.focus617.core.engine.renderer.VertexArray

class XGLRendererAPI : RendererAPI() {

    override fun init() {
        LOG.info("Enable Blend.")
        // 启用颜色混合：这个函数用于决定, 在pixel绘制时, 如果已经有绘制的pixel了,
        // 那么新pixel的权重是其alpha值, 原本的pixel的权重值是1-alpha值
        glEnable(GL_BLEND)

        //设置混合方式，source权重值用其alpha值，destination权重值为1-source权重值
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        //启动深度测试
        glEnable(GL_DEPTH_TEST)
    }

    override fun clear() {
        // 清理屏幕，重绘背景颜色
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT or GLES31.GL_DEPTH_BUFFER_BIT)
    }

    override fun setClearColor(color: Color) {
        GLES31.glClearColor(color.r, color.g, color.b, color.a)
    }

    override fun setViewport(x: Int, y: Int, width: Int, height: Int) {
        // 设置渲染的OpenGL场景（视口）的位置和大小
        GLES31.glViewport(x, y, width, height)
    }

    override fun drawIndexed(vertexArray: VertexArray) {
        // 图元装配，OPENGL ES只支持绘制三角形
        GLES31.glDrawElements(
            GLES31.GL_TRIANGLES,
            (vertexArray as XGLVertexArray).getIndexBuffer()!!.mCount,
            GLES31.GL_UNSIGNED_SHORT, 0
        )
    }
}