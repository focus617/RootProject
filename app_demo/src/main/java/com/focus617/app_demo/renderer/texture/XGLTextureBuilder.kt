package com.focus617.app_demo.renderer.texture

import android.content.Context
import android.opengl.GLES31.GL_TEXTURE_2D
import android.opengl.GLES31.GL_TEXTURE_2D_MULTISAMPLE
import com.focus617.core.engine.renderer.RendererAPI
import com.focus617.mylib.logging.WithLogging

object XGLTextureBuilder : WithLogging() {
    /** 基于Assets中的文件构造 */
    fun createTexture(context: Context, filePath: String): XGLTexture2D? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                LOG.error("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLTexture2D(context, filePath)
        }
    }

    /** 基于Resource/raw中的文件构造 */
    fun createTexture(context: Context, resourceId: Int): XGLTexture2D? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                LOG.error("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLTexture2D(context, resourceId)
        }
    }

    /** 程序编程构造 */
    fun createTexture(width: Int, height: Int): XGLTexture2D? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                LOG.error("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLTexture2D(width, height)
        }
    }

    fun textureTarget(multiSampled: Boolean) =
        if (multiSampled) GL_TEXTURE_2D_MULTISAMPLE else GL_TEXTURE_2D

}