package com.focus617.core.engine.scene_graph.scene

import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.renderer.shader.ShaderLibrary
import com.focus617.core.engine.scene_graph.NodeEntity
import com.focus617.core.engine.scene_graph.components.camera.Camera
import com.focus617.core.engine.scene_graph.components.camera.CameraController
import java.io.Closeable

/**
 * Scene is root entity for all Game Entities.
 */
open class Scene : NodeEntity(), Closeable {
    val mShaderLibrary = ShaderLibrary()

    lateinit var mCamera: Camera
    lateinit var mCameraController: CameraController

    override fun close() {
        mShaderLibrary.close()
    }

    // Used for updating the global resource, such as objects in scene
    override fun onUpdate(timeStep: TimeStep){ }

}