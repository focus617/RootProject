package com.focus617.opengles.egl

import android.opengl.GLSurfaceView.EGLContextFactory
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay

class ContextFactory : EGLContextFactory {

    override fun createContext(
        egl: EGL10,
        display: EGLDisplay?,
        eglConfig: EGLConfig?
    ): EGLContext {

        val EGL_CONTEXT_CLIENT_VERSION = 0x3098

        // EGL_CONTEXT_CLIENT_VERSION set the version of OpenGL ES
        // EGL10.EGL_NONE is used to signify the end of the list.
        val attribList = intArrayOf(EGL_CONTEXT_CLIENT_VERSION, 3, EGL10.EGL_NONE)

        // An EGLContext is a data structure that is used internally by OpenGL.
        val context: EGLContext =
            egl.eglCreateContext(display, eglConfig, EGL10.EGL_NO_CONTEXT, attribList)

        return context
    }

    override fun destroyContext(egl: EGL10, display: EGLDisplay?, context: EGLContext?) {
        egl.eglDestroyContext(display, context)
    }
}