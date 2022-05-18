package com.focus617.app_demo.terrain

import android.opengl.GLES31
import com.focus617.core.engine.objects.d3.Cube
import com.focus617.core.engine.renderer.shader.Shader

class SkyBox : Cube() {
    init {
        shaderName = SkyBoxShaderFilePath
    }

    companion object{
        val SKYBOX_SHADER_PATH = "SkyBox"
        private val SKYBOX_SHADER_FILE = "SkyBox.glsl"

        val SkyBoxShaderFilePath: String = "$SKYBOX_SHADER_PATH/$SKYBOX_SHADER_FILE"
        val SkyBoxTextureFilePath: String = "$SKYBOX_SHADER_PATH/SkyBox"

        const val U_TEXTURE_UNIT = "u_TextureUnit"
        const val textureIndexCubeMap = 0 // skybox always use slot 0
    }

    override fun submit(shader: Shader) {
        shader.setInt(U_TEXTURE_UNIT, textureIndexCubeMap)

        // This avoids problems with the skybox itself getting clipped.
        GLES31.glDepthFunc(GLES31.GL_LEQUAL)

//        meshRenderer.onRender(shader, mTransform)
        onRender(shader)

        GLES31.glDepthFunc(GLES31.GL_LESS)


    }
}