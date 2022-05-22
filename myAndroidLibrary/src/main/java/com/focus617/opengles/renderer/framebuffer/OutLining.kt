package com.focus617.opengles.renderer.framebuffer

import android.opengl.GLES31.*
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.renderer.XRenderer.SceneData
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.resource.baseDataType.Color
import com.focus617.core.engine.scene_graph.GeometryEntity
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_PROJECT_MATRIX
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_VIEW_MATRIX

const val U_COLOR = "u_Color"

fun GeometryEntity.submitWithOutlining(
    shader: Shader,
    color: Color = Color.CYAN,
    scaleSize: Vector3 = Vector3(1.05f, 1.05f, 1.05f)
) {
    // 开启模板测试
    glEnable(GL_STENCIL_TEST)
    glEnable(GL_DEPTH_TEST)

    // 1st. Render pass, draw objects as normal, filling the stencil buffer
    glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE)  // 设置模板、深度测试通过或失败时才采取动作
    glStencilFunc(GL_ALWAYS, 1, 0xFF)          // 绘制的所有片段都要用模板值1更新模板缓冲
    glStencilMask(0xFF)                        // 在正常绘制时确保打开模板缓冲的写入

    shader.bind()
    this.onRender(shader)

    // 2nd. Render pass, now draw slightly scaled versions of the objects,
    // this time disabling stencil writing.
    // Because stencil buffer is now filled with several 1s. The parts of the buffer
    // that are 1 are now not drawn, thus only drawing the objects' size differences,
    // making it look like borders.
    glStencilFunc(GL_NOTEQUAL, 1, 0xFF)     // 只绘制外围不等于1的那部分
    glStencilMask(0x00)
    //glDisable(GL_DEPTH_TEST)

    with(FrameBufferEntity.shaderOutlining) {
        bind()
        setMat4(U_PROJECT_MATRIX, SceneData.sProjectionMatrix)
        setMat4(U_VIEW_MATRIX, SceneData.sViewMatrix)
        setFloat4(U_COLOR, color)
    }
    this.push()
    this.scale(scaleSize)
    this.onRender(FrameBufferEntity.shaderOutlining)
    this.pop()

// 关闭模板测试
    glDisable(GL_STENCIL_TEST)
    glEnable(GL_DEPTH_TEST)

}

