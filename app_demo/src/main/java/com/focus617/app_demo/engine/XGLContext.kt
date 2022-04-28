package com.focus617.app_demo.engine

import android.opengl.GLES20.*
import android.opengl.GLES30
import com.focus617.core.engine.renderer.IfGraphicsContext
import com.focus617.core.platform.base.BaseEntity


class XGLContext(private val windowHandle: AndroidWindow) : BaseEntity(), IfGraphicsContext {
    var isES3Supported: Boolean = false

    override fun init() = with(windowHandle) {
        // Check if the system supports OpenGL ES 3.0.
        if (isES3Supported) {
            // Request an OpenGL ES 3.0 compatible context.
            setEGLContextClientVersion(3)
            setEGLConfigChooser(MultiSampleConfigChooser())
        } else {
            // Request an OpenGL ES 2.0 compatible context.
            setEGLContextClientVersion(2)
            setEGLConfigChooser(true)
        }

        requestFocus()                   //获取焦点
        isFocusableInTouchMode = true    //设置为可触控

    }

    override fun swapBuffers() {
        windowHandle.requestRender()
    }

    companion object {
        fun getOpenGLInfo() {
            LOG.info("OpenGL Info")
            LOG.info("OpenGL Vendor  : ${glGetString(GL_VENDOR)}")
            LOG.info("OpenGL Renderer: ${glGetString(GL_RENDERER)}")
            LOG.info("OpenGL Version : ${glGetString(GL_VERSION)}")
        }

        fun checkGLError() {
            val error = GLES30.glGetError()
            if (error != GLES30.GL_NO_ERROR) {
                val hexErrorCode = Integer.toHexString(error)
                LOG.error("glError: $hexErrorCode")
                throw RuntimeException("GLError")
            }
        }
    }
}