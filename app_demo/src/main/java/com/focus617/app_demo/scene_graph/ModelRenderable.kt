package com.focus617.app_demo.scene_graph

import android.content.Context
import com.focus617.app_demo.renderer.texture.XGLTextureBuilder
import com.focus617.app_demo.renderer.vertex.XGLIndexBuffer
import com.focus617.app_demo.renderer.vertex.XGLVertexArray
import com.focus617.app_demo.renderer.vertex.XGLVertexBuffer
import com.focus617.app_demo.renderer.vertex.XGLVertexBufferBuilder
import com.focus617.core.engine.scene_graph.renderer.Material
import com.focus617.core.engine.scene_graph.renderer.Mesh
import com.focus617.core.engine.scene_graph.renderer.Renderable
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_MATERIAL_TEXTURE_DIFFUSE
import com.focus617.platform.objLoader.MtlLoader
import com.focus617.platform.objLoader.ObjLoader

/**
 * Renders a 3D Model by attaching it to a {@link NodeEntity} with setRenderable(Renderable).
 */
class ModelRenderable private constructor() : Renderable() {


    companion object{
        fun builder(context: Context, filePath: String): ModelRenderable {
            LOG.info("load Obj model from $filePath")
            val model = ModelRenderable()

            // Retrieve Mesh List
            val meshList = ObjLoader.loadOBJ(context, filePath).toVertexArrayModel()
            // Retrieve Material List
            MtlLoader.loadMtl(context, ObjLoader.getMtlFilePath()).dump()

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

                model.mMeshes[it.textureName] = Mesh(vertexArray)
                LOG.info("load Mesh: texture=${it.textureName}, indiceSize=${it.indices.size}")
            }

            // Build Material
            for((key, materialInfo) in MtlLoader.mMtlMap){
                LOG.info("load material name = $key")
                val material = Material()
                model.mMaterials[key] = material

                materialInfo.Kd_Texture?.apply {
                    val textureName = "${MtlLoader.directory}/${materialInfo.Kd_Texture}"
                    LOG.info("Create KD texture: $textureName")

                    val texture = XGLTextureBuilder.createTexture(
                        context, textureName
                    )!!
                    material.add(U_MATERIAL_TEXTURE_DIFFUSE, texture)
//                    val textureIndex = XGLTextureSlots.requestIndex(texture)
                }

                materialInfo.Ka_Texture?.apply {
                    val textureName = "${MtlLoader.directory}/${materialInfo.Ka_Texture}"
                    LOG.info("Need create KA texture: $textureName")
                }

                materialInfo.Ks_ColorTexture?.apply {
                    val textureName = "${MtlLoader.directory}/${materialInfo.Ks_ColorTexture}"
                    LOG.info("Need create KsColor texture: $textureName")
                }
            }

            // Release memory
            ObjLoader.clear()
            MtlLoader.clear()

            return model
        }
    }

}