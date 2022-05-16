package com.focus617.app_demo.engine.d3

import android.content.Context
import com.focus617.app_demo.engine.XGLDrawableObject
import com.focus617.app_demo.renderer.texture.XGLTextureBuilder
import com.focus617.app_demo.renderer.vertex.XGLVertexArray
import com.focus617.core.engine.objects.DynamicCreationObject
import com.focus617.core.engine.renderer.texture.Texture2D
import com.focus617.core.engine.renderer.vertex.VertexArray
import com.focus617.core.engine.scene.Material
import com.focus617.platform.helper.FileHelper
import com.focus617.platform.objTools.*
import java.io.Closeable

class Model(
    private val context: Context,
    private val filePath: String
) : DynamicCreationObject(), XGLDrawableObject, Closeable {

    //模型所包含的Mesh集合
    private val mMeshes = HashMap<String, VertexArray>()

    //模型所包含的Material集合
    private val mMaterials = HashMap<String, Material>()

    // 对所有加载过的纹理全局储存，防止重复加载
    private val texturesLoaded = HashMap<String, Texture2D>()

    //模型文件所在目录
    private lateinit var directory: String

    override fun beforeBuild() {
        LOG.info("load Obj model from $filePath")
        directory = FileHelper.getPath(filePath)

        val objInfo = ObjLoader.load(context, filePath)
        loadMaterialTextures(objInfo.mMaterialInfos)
        processMesh(objInfo)
    }

    override fun initOpenGlResource() {
        vertexArray = XGLVertexArray.buildVertexArray(this)
    }

    override fun close() {
        mMeshes.clear()
        mMaterials.clear()
        texturesLoaded.clear()
    }

    //创建纹理并加载图像数据
    private fun loadMaterialTextures(mMaterialMap: HashMap<String, MaterialInfo>) {

        if (mMaterialMap.size == 0) {
            val defaultPath = "$directory/$defaultTextureFile"
            val defaultTextureId =
                XGLTextureBuilder.createTexture(context, defaultPath)

            //将构建的textureId存进已构建纹理库
            texturesLoaded[defaultPath] = defaultTextureId!!

            // 构建缺省材料
            val defaultMaterial = Material(DEFAULT_GROUP_NAME)
            defaultMaterial.textureDiffuse = defaultTextureId.mHandle

            mMaterials[DEFAULT_GROUP_NAME] = defaultMaterial

        } else {
            for ((materialName, materialInfo) in mMaterialMap) {

                val textureDiffuse =
                    if (materialInfo.Kd_Texture != null)
                        getTexture("$directory/${materialInfo.Kd_Texture}")
                    else 0

                val textureSpecular =
                    if (materialInfo.Ks_ColorTexture != null)
                        getTexture("$directory/${materialInfo.Ks_ColorTexture}")
                    else 0

                // 构建材料
                val material = Material(materialName)
                material.ambient = materialInfo.Ka_Color.toVector3()
                material.diffuse = materialInfo.Kd_Color.toVector3()
                material.specular = materialInfo.Ks_Color.toVector3()
                material.shininess = materialInfo.ns

                material.textureDiffuse = textureDiffuse
                material.textureSpecular = textureSpecular

                mMaterials[materialName] = material
            }
        }

    }

    private fun getTexture(filePath: String): Int =
        //如果纹理库中还没有需要构建的纹理，创建新的纹理
        if (texturesLoaded[filePath] == null) {
            val texture = XGLTextureBuilder.createTexture(context, filePath)
            //将构建的textureId存进已构建纹理库
            texturesLoaded[filePath] = texture!!
            texture.mHandle
        }
        //如果纹理库中已有构建好的纹理，直接使用
        else texturesLoaded[filePath]!!.mHandle

    //生成网格
    private fun processMesh(objInfo: ObjInfo) {

        val dataList = objInfo.translate()
        objInfo.clear()

        for ((key, data) in dataList) {
//            mMeshes[key] = VertexArray(context, key, data)
        }
    }
}