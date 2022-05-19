package com.focus617.app_demo.scene_graph

import android.content.Context
import com.focus617.app_demo.renderer.vertex.XGLIndexBuffer
import com.focus617.app_demo.renderer.vertex.XGLVertexArray
import com.focus617.app_demo.renderer.vertex.XGLVertexBuffer
import com.focus617.app_demo.renderer.vertex.XGLVertexBufferBuilder
import com.focus617.core.engine.renderer.texture.Texture2D
import com.focus617.core.engine.scene_graph.NodeEntity
import com.focus617.core.engine.scene_graph.renderer.Mesh
import com.focus617.platform.objLoader.MtlLoader
import com.focus617.platform.objLoader.ObjLoader
import java.io.Closeable

class Model(
    private val context: Context,
    private val filePath: String
) : NodeEntity(), Closeable {

    //模型所包含的Mesh集合
    private val mMeshes = HashMap<String, Mesh>()

    //模型所包含的Material集合
//    private val mMaterials = HashMap<String, Material>()

    // 对所有加载过的纹理全局储存，防止重复加载
    private val texturesLoaded = HashMap<String, Texture2D>()


    fun initUnderOpenGl() {
        LOG.info("load Obj model from $filePath")

        // Retrieve Mesh List
        val meshList = ObjLoader.loadOBJ(context, filePath).toVertexArrayModel()
        // Retrieve Material List
        MtlLoader.loadMtl(context, ObjLoader.getMtlFilePath())
        // Release memory
        ObjLoader.clear()
        MtlLoader.clear()

        // Build Mesh
        meshList.forEach {
            val vertexArray =
                XGLVertexBufferBuilder.createVertexArray() as XGLVertexArray

            val vertexBuffer = XGLVertexBufferBuilder.createVertexBuffer(
                it.vertices, it.vertices.size * Float.SIZE_BYTES
            ) as XGLVertexBuffer
            vertexBuffer.setLayout(it.layout)
            vertexArray.addVertexBuffer(vertexBuffer)

            val indexBuffer = XGLVertexBufferBuilder.createIndexBuffer(
                it.indices, it.indices.size
            ) as XGLIndexBuffer
            vertexArray.setIndexBuffer(indexBuffer)

            mMeshes[it.textureName] = Mesh(vertexArray)
        }
        // Build Material
    }


    override fun close() {
        mMeshes.clear()
//        mMaterials.clear()
        texturesLoaded.clear()
    }
}