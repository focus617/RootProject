package com.focus617.opengles.terrain

import android.content.Context
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Vector3
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher
import com.focus617.opengles.scene_graph.XGLDrawableObject
import timber.log.Timber

class TerrainLayer(name: String, private val context: Context) : Layer(name) {
    private val eventDispatcher = EventDispatcher()

    init {
        val heightmapMesh = HeightmapMesh(context, Heightmap.HeightMapBitmapFilePath)
        val heightmap = Heightmap(heightmapMesh)
        // Expand the heightmap's dimensions, but don't expand the height as
        // much so that we don't get insanely tall mountains.
        heightmap.onTransform3D(
            Vector3(0.0f, 0.0f, 0.0f),
            Vector3(100f, 10f, 100f)
        )
        gameObjectList.add(heightmap)

        val skyBox = SkyBox()
        skyBox.onTransform3D(
            Vector3(0.0f, 0.0f, 0.0f),
            Vector3(100f, 100f, 100f)
        )
        gameObjectList.add(skyBox)
    }

    override fun initOpenGlResource() {
        for (gameObject in gameObjectList) {
//            val mesh = Mesh(XGLVertexArray.buildVertexArray(gameObject))
//            val meshRenderer = MeshRenderer(mesh, Material())
//            gameObject.addComponent(meshRenderer)
            (gameObject as XGLDrawableObject).initOpenGlResource()
        }
    }

    override fun close() {
        Timber.i("${this.mDebugName} closed")
        eventDispatcher.close()
    }

    override fun onAttach() {
        Timber.i("${this.mDebugName} onAttach()")
    }

    override fun onDetach() {
        Timber.i("${this.mDebugName} onDetach")
    }

    override fun onUpdate(timeStep: TimeStep) {
        //Timber.i("${this.mDebugName} onUpdate")
    }

    override fun beforeDrawFrame() {
    }

    override fun afterDrawFrame() {
    }

    override fun onEvent(event: Event): Boolean {
        return eventDispatcher.dispatch(event)
    }

}
