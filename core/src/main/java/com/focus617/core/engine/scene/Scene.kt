package com.focus617.core.engine.scene

import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.renderer.ShaderLibrary
import com.focus617.core.platform.base.BaseEntity
import java.io.Closeable

open class Scene : BaseEntity(), Closeable {
    val mShaderLibrary = ShaderLibrary()

    lateinit var mCamera: Camera
    lateinit var mCameraController: CameraController

    override fun close() {
        mShaderLibrary.close()
    }

    // Used for updating the global resource, such as objects in scene
    open fun onUpdate(timeStep: TimeStep){ }

}