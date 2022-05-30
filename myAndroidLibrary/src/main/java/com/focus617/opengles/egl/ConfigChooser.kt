package com.focus617.opengles.egl

import android.opengl.EGL14.EGL_OPENGL_ES2_BIT
import android.opengl.GLSurfaceView
import timber.log.Timber
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLDisplay


class ConfigChooser : GLSurfaceView.EGLConfigChooser {

    override fun chooseConfig(egl: EGL10, display: EGLDisplay): EGLConfig? {

        // to send the desired configuration that I want
        val configAttributes = intArrayOf(
            EGL10.EGL_RED_SIZE, redSize,
            EGL10.EGL_GREEN_SIZE, greenSize,
            EGL10.EGL_BLUE_SIZE, blueSize,
            EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
            EGL10.EGL_DEPTH_SIZE, depthSize,
            EGL10.EGL_STENCIL_SIZE, stencilSize,
//            EGL10.EGL_SAMPLES, sampleSize,
            EGL10.EGL_NONE
        )
        val numConfig = IntArray(1)
        require(
            egl.eglChooseConfig(display, configAttributes, null, 0, numConfig)
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

                if (r == redSize && g == greenSize
                    && b == blueSize && a == alphaSize
                    && d >= depthSize && s >= stencilSize
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
        egl.eglGetConfigAttrib(display, config, attribute, value)
    ) value[0]
    else defaultValue

    companion object {
        // Surface format: RGBA8888
        var redSize = 8
        var greenSize = 8
        var blueSize = 8
        var alphaSize = 8
        var depthSize = 16
        var stencilSize = 0
        var sampleSize = 4        // enabling Anti Aliasing

        var value = IntArray(1)
    }

}