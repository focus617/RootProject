package com.focus617.app_demo.terrain

import android.opengl.GLES31
import com.focus617.app_demo.renderer.XGLTextureCubeMap
import com.focus617.core.engine.objects.d3.Cube
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.Shader
import com.focus617.core.engine.renderer.TextureLibrary

class SkyBox : Cube() {

    companion object{
        val SKYBOX_SHADER_PATH = "SkyBox"
        private val SKYBOX_SHADER_FILE = "SkyBox.glsl"

        val SkyBoxShaderFilePath: String = "$SKYBOX_SHADER_PATH/$SKYBOX_SHADER_FILE"
        val SkyBoxTextureFilePath: String = "$SKYBOX_SHADER_PATH/SkyBox"

        const val U_TEXTURE_UNIT = "u_TextureUnit"
    }

    override fun submit(lib: TextureLibrary, shader: Shader) {
        val texture = lib.get(SkyBoxTextureFilePath) as XGLTextureCubeMap
        texture.bind(texture.textureObjectId)

        shader.bind()
        shader.setMat4(U_MODEL_MATRIX, modelMatrix)
        shader.setInt(U_TEXTURE_UNIT, texture.textureObjectId)

        // This avoids problems with the skybox itself getting clipped.
        GLES31.glDepthFunc(GLES31.GL_LEQUAL)

        vertexArray.bind()
        RenderCommand.drawIndexed(vertexArray)

        GLES31.glDepthFunc(GLES31.GL_LESS)

        // 下面这两行可以省略，以节约GPU的运行资源；
        // 在下个submit，会bind其它handle，自然会实现unbind
        vertexArray.unbind()
        shader.unbind()
    }
}