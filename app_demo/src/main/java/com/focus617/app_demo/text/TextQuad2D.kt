package com.focus617.app_demo.text

import com.focus617.app_demo.renderer.framebuffer.FrameBufferQuad
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.resource.baseDataType.Color

class TextQuad2D: FrameBufferQuad() {
    var fontColor: Color = Color.BLACK

    // 用于本对象作为一个GO时（目前就用在overLayer的字幕）
    override fun submit(shader: Shader) {
        shaderWithColor.setFloat4(U_COLOR, fontColor)

//        mesh.draw()
//        shaderWithColor.unbind()
        meshRenderer.onRender(shaderWithColor, mTransform)

    }

    companion object {
        const val SHADER_PATH = "framebuffer"
        const val SHADER_COLOR_FILE = "shaderWithColor.glsl"

        const val ShaderWithColorFilePath: String = "$SHADER_PATH/$SHADER_COLOR_FILE"

        lateinit var shaderWithColor: Shader

        const val U_TEXTURE = "u_screenTexture"
        const val U_COLOR = "u_Color"
    }
}