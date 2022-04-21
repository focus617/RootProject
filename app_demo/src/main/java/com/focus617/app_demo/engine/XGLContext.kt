package com.focus617.app_demo.engine

import android.opengl.GLES20.*
import com.focus617.core.engine.renderer.IfGraphicsContext
import com.focus617.core.platform.base.BaseEntity


class XGLContext(private val windowHandle: AndroidWindow) : BaseEntity(), IfGraphicsContext {

    override fun init() = with(windowHandle) {
        // Check if the system supports OpenGL ES 3.0.
        if (isES3Supported) {
            // Request an OpenGL ES 3.0 compatible context.
            setEGLContextClientVersion(3)
        } else {
            // Request an OpenGL ES 2.0 compatible context.
            setEGLContextClientVersion(2)
        }

        /**
         * 一个给定的Android设备可能支持多个EGLConfig渲染配置。
         * 可用的配置可能在有多少个数据通道和分配给每个数据通道的比特数上不同。
         * 默认情况下，GLSurfaceView选择的EGLConfig有RGB_888像素格式，至少有16位深度缓冲和没有模板。
         * 安装一个ConfigChooser，它将至少具有指定的depthSize和stencilSize的配置，并精确指定redSize、
         * greenSize、blueSize和alphaSize(Alpha used for plane blending)。
         */
        //setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        setEGLConfigChooser(true)
        requestFocus()                   //获取焦点
        isFocusableInTouchMode = true    //设置为可触控

    }

    override fun swapBuffers() {
        windowHandle.requestRender()
    }

    fun getOpenGLInfo(){
        LOG.info("OpenGL Info")
        LOG.info("OpenGL Vendor  : ${glGetString(GL_VENDOR)}")
        LOG.info("OpenGL Renderer: ${glGetString(GL_RENDERER)}")
        LOG.info("OpenGL Version : ${glGetString(GL_VERSION)}")
    }

}