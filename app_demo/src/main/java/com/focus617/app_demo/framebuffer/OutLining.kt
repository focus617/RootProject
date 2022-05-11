package com.focus617.app_demo.framebuffer

import android.opengl.GLES31.*
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.objects.DrawableObject
import com.focus617.core.engine.renderer.Shader

fun DrawableObject.submitWithOutlining(
    shader: Shader,
    scaleSize: Vector3 = Vector3(1.1f, 1.1f, 1.1f)
) {
    // 1st. Render pass, draw objects as normal, filling the stencil buffer
    glStencilFunc(GL_ALWAYS, 1, 0xFF)   // 绘制的所有片段都要用模板值1更新模板缓冲
    glStencilMask(0xFF)                     // 在正常绘制时确保打开模板缓冲的写入
    glEnable(GL_DEPTH_TEST)

    shader.bind()
    this.submit(shader)

    // 2nd. Render pass, now draw slightly scaled versions of the objects,
    // this time disabling stencil writing.
    // Because stencil buffer is now filled with several 1s. The parts of the buffer
    // that are 1 are now not drawn, thus only drawing the objects' size differences,
    // making it look like borders.
    glStencilFunc(GL_NOTEQUAL, 1, 0xFF)     // 只绘制外围不等于1的那部分
    glStencilMask(0x00)
    glDisable(GL_DEPTH_TEST)

    this.scale(scaleSize)
    val outliningShader = FrameBufferQuad.shaderOutlining
    outliningShader.bind()
    this.submit(outliningShader)

    glStencilMask(0xFF)
    glEnable(GL_DEPTH_TEST)
}

