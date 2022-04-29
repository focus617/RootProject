package com.focus617.app_demo.engine

import android.opengl.GLES30
import android.opengl.GLES32
import android.os.Build
import androidx.annotation.RequiresApi
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
            LOG.info("OpenGL Vendor  : ${GLES30.glGetString(GLES30.GL_VENDOR)}")
            LOG.info("OpenGL Renderer: ${GLES30.glGetString(GLES30.GL_RENDERER)}")
            LOG.info("OpenGL Version : ${GLES30.glGetString(GLES30.GL_VERSION)}")
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun initDebug() {
            GLES32.glEnable(GLES32.GL_DEBUG_OUTPUT)
            GLES32.glEnable(GLES32.GL_DEBUG_OUTPUT_SYNCHRONOUS)
            GLES32.glDebugMessageCallback { source, type, id, severity, message ->
                when (severity) {
                    GLES32.GL_DEBUG_SEVERITY_HIGH -> LOG.error(
                        "%d: %s of %s severity, raised from %s: %s\n",
                        id, type, severity, source, message
                    )
                    GLES32.GL_DEBUG_SEVERITY_MEDIUM -> LOG.warn(
                        "%d: %s of %s severity, raised from %s: %s\n",
                        id, type, severity, source, message
                    )
                    GLES32.GL_DEBUG_SEVERITY_LOW -> LOG.info(
                        "%d: %s of %s severity, raised from %s: %s\n",
                        id, type, severity, source, message
                    )
                    GLES32.GL_DEBUG_SEVERITY_NOTIFICATION -> LOG.trace(
                        "%d: %s of %s severity, raised from %s: %s\n",
                        id, type, severity, source, message
                    )
                    else -> {
                        LOG.debug("onMessage from OpenGl: Unknown severity level!")
                        LOG.debug(
                            "%d: %s of %s severity, raised from %s: %s\n",
                            id, type, severity, source, message
                        )
                    }
                }
            }

            GLES32.glDebugMessageControl(
                GLES32.GL_DONT_CARE,
                GLES32.GL_DONT_CARE,
                GLES32.GL_DEBUG_SEVERITY_NOTIFICATION,
                0,
                null,
                false
            )
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