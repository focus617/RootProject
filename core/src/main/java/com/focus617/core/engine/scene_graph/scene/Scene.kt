package com.focus617.core.engine.scene_graph.scene

import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.renderer.shader.ShaderLibrary
import com.focus617.core.engine.scene_graph.components.Light
import com.focus617.core.engine.scene_graph.components.camera.Camera
import com.focus617.core.engine.scene_graph.components.camera.CameraController
import com.focus617.core.engine.scene_graph.core.IfEntity
import com.focus617.core.engine.scene_graph.core.ParentEntity
import com.focus617.core.platform.event.base.Event
import java.io.Closeable

/**
 * Scene is root entity for all Game Entities.
 */
abstract class Scene : ParentEntity(), IfEntity, Closeable {
    val mShaderLibrary = ShaderLibrary()

    lateinit var mLight: Light
    lateinit var mCamera: Camera
    lateinit var mCameraController: CameraController

    override fun close() {
        mShaderLibrary.close()
    }

    override fun onEvent(event: Event) {}

    // Used for updating the global resource, such as objects in scene
    override fun onUpdate(timeStep: TimeStep) {}

    override fun onRender(shader: Shader) {
        mComponents.forEach {
            it.onRender(shader, null)
        }
    }

}