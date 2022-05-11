package com.focus617.app_demo.renderer

import android.opengl.GLES31
import android.opengl.GLES31.*
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
        glDepthFunc(GL_LEQUAL) // indicate what type of depth test

        //Define Cull Back Face, Enable/Disable by each Layer/GO
        glCullFace(GLES31.GL_BACK)
    }

    override fun clear() {
        // 清理屏幕，重绘背景颜色
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)
        glClearDepthf(1.0f)     // Setup the Depth buff
        glClearStencil(0)           // Setup the Stencil buff
    }

    override fun setClearColor(color: Color) {
        glClearColor(color.r, color.g, color.b, color.a)
    }

    override fun setViewport(x: Int, y: Int, width: Int, height: Int) {
        // 设置渲染的OpenGL场景（视口）的位置和大小
        glViewport(x, y, width, height)
    }

    override fun drawIndexed(vertexArray: VertexArray, indexCount: Int) {
        val count =
            if (indexCount != 0) indexCount
            else (vertexArray as XGLVertexArray).getIndexBuffer()!!.mCount

        // 图元装配，OPENGL ES只支持绘制三角形
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_SHORT, 0)
        glBindTexture(GL_TEXTURE_2D, 0)
    }
}