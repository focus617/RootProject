package com.focus617.core.engine.scene_graph.renderer

import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.renderer.texture.Texture


class Material() {
    private val mFloatHashMap = HashMap<String, Float>()
    private val mVector3HashMap = HashMap<String, Vector3>()
    private val mTextureHashMap = HashMap<String, Texture>()
    private val mTextureIndexHashMap = HashMap<String, Int>()

    constructor(
        diffuse: Texture, specularIntensity: Float, specularPower: Float, normal: Texture,
        dispMap: Texture, dispMapScale: Float, dispMapOffset: Float
    ): this() {
        add("diffuse", diffuse)
        add("specularIntensity", specularIntensity)
        add("specularPower", specularPower)     // 镜面强度(Specular Intensity)
        add("normalMap", normal)
        add("dispMap", dispMap)
        val baseBias = dispMapScale / 2.0f
        add("dispMapScale", dispMapScale)
        add("dispMapBias", -baseBias + baseBias * dispMapOffset)
    }

    fun getFloat(name: String): Float?  = mFloatHashMap[name]
    fun getVector3(name: String): Vector3?  = mVector3HashMap[name]
    fun getTexture(name: String): Texture?  = mTextureHashMap[name]
    fun getTextureIndex(name: String): Int?  = mTextureIndexHashMap[name]

    fun add(name: String, value: Float) {
        mFloatHashMap[name] = value
    }
    fun add(name: String, value: Vector3) {
        mVector3HashMap[name] = value
    }
    fun add(name: String, texture: Texture) {
        mTextureHashMap[name] = texture
    }
    fun add(name: String, value: Int) {
        mTextureIndexHashMap[name] = value
    }

    fun onRender(shader: Shader){
        for((key, value) in mFloatHashMap){
            shader.setFloat(key, value)
        }

        for((key, value) in mVector3HashMap){
            shader.setFloat3(key, value)
        }

        for((key, textureIndex) in mTextureIndexHashMap){
            shader.setInt(key, textureIndex)
        }
    }
}
