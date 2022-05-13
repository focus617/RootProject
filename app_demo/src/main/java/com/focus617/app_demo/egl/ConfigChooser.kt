package com.focus617.app_demo.egl

import android.opengl.GLSurfaceView
import com.focus617.app_demo.engine.AndroidWindow
import timber.log.Timber
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLDisplay


class ConfigChooser : GLSurfaceView.EGLConfigChooser {
    private val numConfig = IntArray(1)

    override fun chooseConfig(egl: EGL10, display: EGLDisplay): EGLConfig? {
        val EGL_OPENGL_ES2_BIT = 4

        // to send the desired configuration that I want
        val configAttributes = intArrayOf(
            EGL10.EGL_RED_SIZE, AndroidWindow.redSize,
            EGL10.EGL_GREEN_SIZE, AndroidWindow.greenSize,
            EGL10.EGL_BLUE_SIZE, AndroidWindow.blueSize,
            EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
            EGL10.EGL_SAMPLES, AndroidWindow.sampleSize,
            EGL10.EGL_DEPTH_SIZE, AndroidWindow.depthSize,
            EGL10.EGL_STENCIL_SIZE, AndroidWindow.stencilSize,
            EGL10.EGL_NONE
        )
        require(
            egl.eglChooseConfig(
                display, configAttributes, null, 0, numConfig
            )
        ) { "eglChooseConfig failed" }

        val numConfigs = numConfig[0]   // returns number of configs available
        if (numConfigs <= 0) {
            Timber.e("No match config was found. ")
        }

        // to hold all the configurations that match what I require
        val configs: Array<EGLConfig?> = arrayOfNulls<EGLConfig>(numConfigs)
        egl.eglChooseConfig(display, configAttributes, configs, numConfigs, numConfig)

        return selectConfig(egl, display, configs)
    }

    /*
     * This function loops through all of the configurations that match or exceed my current
     * specification and then makes sure that my configuration is matched exactly.
     */
    private fun selectConfig(
        egl: EGL10, display: EGLDisplay, configs: Array<EGLConfig?>
    ): EGLConfig? {
        for (config in configs) {
            config?.apply {
                val d: Int = findConfigAttrib(egl, display, config, EGL10.EGL_DEPTH_SIZE, 0)
                val s: Int = findConfigAttrib(egl, display, config, EGL10.EGL_GREEN_SIZE, 0)
                val r: Int = findConfigAttrib(egl, display, config, EGL10.EGL_RED_SIZE, 0)
                val g: Int = findConfigAttrib(egl, display, config, EGL10.EGL_GREEN_SIZE, 0)
                val b: Int = findConfigAttrib(egl, display, config, EGL10.EGL_BLUE_SIZE, 0)
                val a: Int = findConfigAttrib(egl, display, config, EGL10.EGL_ALPHA_SIZE, 0)

                if (r == AndroidWindow.redSize && g == AndroidWindow.greenSize
                    && b == AndroidWindow.blueSize && a == AndroidWindow.alphaSize
                    && d >= AndroidWindow.depthSize && s >= AndroidWindow.stencilSize
                )
                    return config
            }
        }
        return null
    }

    // Return value based on key=attribute
    private fun findConfigAttrib(
        egl: EGL10, display: EGLDisplay, config: EGLConfig, attribute: Int, defaultValue: Int
    ): Int = if (
        egl.eglGetConfigAttrib(display, config, attribute, AndroidWindow.value)
    ) AndroidWindow.value[0]
    else defaultValue

}