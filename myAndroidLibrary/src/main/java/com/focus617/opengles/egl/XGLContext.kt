package com.focus617.opengles.egl

import android.opengl.GLES31.*
import android.opengl.GLES32
import android.opengl.GLSurfaceView
import com.focus617.core.engine.renderer.IfGraphicsContext
import com.focus617.core.platform.base.BaseEntity
import timber.log.Timber


class XGLContext(private val windowHandle: GLSurfaceView) : BaseEntity(), IfGraphicsContext {
    var isES3Supported: Boolean = false

    override fun init() = with(windowHandle) {
        // Check if the system supports OpenGL ES 3.0.
        if (isES3Supported) {
            // Request an OpenGL ES 3.0 compatible context.
            setEGLContextClientVersion(3)

//            setEGLConfigChooser(MultiSampleConfigChooser())
            setEGLContextFactory(ContextFactory())
            setEGLConfigChooser(ConfigChooser())

        } else {
            // Request an OpenGL ES 2.0 compatible context.
            setEGLContextClientVersion(2)
            setEGLContextFactory(ContextFactory())
            setEGLConfigChooser(ConfigChooser())
        }

        requestFocus()                   //获取焦点
        isFocusableInTouchMode = true    //设置为可触控
    }

    override fun swapBuffers() {
        windowHandle.requestRender()
    }

    companion object {
        fun getOpenGLInfo() {
            Timber.i("OpenGL Info")
            Timber.i("OpenGL Vendor  : ${glGetString(GL_VENDOR)}")
            Timber.i("OpenGL Renderer: ${glGetString(GL_RENDERER)}")
            Timber.i("OpenGL Version : ${glGetString(GL_VERSION)}")
        }

        fun checkGLError(msg: String) {
            val error = glGetError()
            if (error != GL_NO_ERROR) {
                val hexErrorCode = Integer.toHexString(error)
                Timber.w("Check glError $msg errorCode:$hexErrorCode")
                //throw RuntimeException("GLError")
            }
        }

        // The extensions string is padded with spaces between extensions, but not
        // necessarily at the beginning or end. For simplicity, add spaces at the
        // beginning and end of the extensions string and the extension string.
        // This means we can avoid special-case checks for the first or last
        // extension, as well as avoid special-case checks when an extension name
        // is the same as the first part of another extension name.
        fun checkIfContextSupportsExtension(extension: String): Boolean {
            val extensions = " " + glGetString(GL_EXTENSIONS) + " "
            return extensions.indexOf(" $extension ") >= 0
        }


        fun initDebug() {
            GLES32.glEnable(GLES32.GL_DEBUG_OUTPUT)
            GLES32.glEnable(GLES32.GL_DEBUG_OUTPUT_SYNCHRONOUS)
            GLES32.glDebugMessageCallback { source, type, id, severity, message ->
                when (severity) {
                    GLES32.GL_DEBUG_SEVERITY_HIGH -> Timber.e(
                        "%d: %s of %s severity, raised from %s: %s\n",
                        id, type, severity, source, message
                    )
                    GLES32.GL_DEBUG_SEVERITY_MEDIUM -> Timber.w(
                        "%d: %s of %s severity, raised from %s: %s\n",
                        id, type, severity, source, message
                    )
                    GLES32.GL_DEBUG_SEVERITY_LOW -> Timber.i(
                        "%d: %s of %s severity, raised from %s: %s\n",
                        id, type, severity, source, message
                    )
                    GLES32.GL_DEBUG_SEVERITY_NOTIFICATION -> Timber.i(
                        "%d: %s of %s severity, raised from %s: %s\n",
                        id, type, severity, source, message
                    )
                    else -> {
                        Timber.d("onMessage from OpenGl: Unknown severity level!")
                        Timber.d(
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
    }
}