package com.focus617.app_demo.scene_graph

import android.content.Context
import com.focus617.core.engine.objects.GeneratedData
import com.focus617.core.engine.renderer.texture.Texture2D
import com.focus617.core.engine.renderer.vertex.BufferLayout
import com.focus617.core.engine.renderer.vertex.VertexArray
import com.focus617.core.engine.scene_graph.renderer.IfModelLoader
import com.focus617.core.platform.base.BaseEntity
import com.focus617.platform.helper.FileHelper
import com.focus617.platform.objTools.ObjLoader
import java.io.Closeable

class Model(
    private val context: Context,
    private val filePath: String
) : BaseEntity(), IfModelLoader, Closeable {

    //模型所包含的Mesh集合
    private val mMeshes = HashMap<String, VertexArray>()

//    //模型所包含的Material集合
//    private val mMaterials = HashMap<String, Material>()

    // 对所有加载过的纹理全局储存，防止重复加载
    private val texturesLoaded = HashMap<String, Texture2D>()

    //模型文件所在目录
    private lateinit var directory: String

    //分析模型文件后生成的临时数据（Vertices and Indices）
    var buildData: GeneratedData? = null

    override fun beforeBuild() {
        LOG.info("load Obj model from $filePath")
        directory = FileHelper.getPath(filePath)
//
        val objInfo = ObjLoader.load(context, filePath)
//        loadMaterialTextures(objInfo.mMaterialInfos)
//        processMesh(objInfo)
    }

    override fun afterBuild() {
        buildData = null
    }

    override fun getVertices(): FloatArray {
        if (buildData == null)
            BaseEntity.LOG.error("You should call beforeBuild at first.")
        return buildData!!.vertices
    }

    override fun getLayout(): BufferLayout {
        if (buildData == null)
            BaseEntity.LOG.error("You should call beforeBuild at first.")
        return buildData!!.layout
    }

    override fun getIndices(): ShortArray {
        if (buildData == null)
            BaseEntity.LOG.error("You should call beforeBuild at first.")
        return buildData!!.indices
    }

    override fun close() {
        buildData = null

        mMeshes.clear()
//        mMaterials.clear()
        texturesLoaded.clear()
    }
}