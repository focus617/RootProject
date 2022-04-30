package com.focus617.core.engine.scene

import com.focus617.core.engine.objects.DrawableObject
import com.focus617.core.engine.objects.d3.Cylinder
import com.focus617.core.engine.renderer.ShaderLibrary
import com.focus617.core.engine.renderer.Texture
import com.focus617.core.platform.base.BaseEntity
import java.io.Closeable

class Scene(val is3D: Boolean, val mCamera: Camera) : BaseEntity(), Closeable {
    private val textureSet = HashMap<String, Texture>()
    val mShaderLibrary = ShaderLibrary()

    val gameObjectList = mutableListOf<DrawableObject>()

    init {
        //gameObjectList.add(Circle(1.0f))
        //gameObjectList.add(Cone(1.0f, 1.0f))
        gameObjectList.add(Cylinder(1.0f, 1.0f))
        //gameObjectList.add(Ball(1.0f))
        //gameObjectList.add(Star(5, 0.3f, 1.0f, 0f))
    }


    fun texture(name: String) = textureSet[name]
    fun sizeForTest() = textureSet.size
    fun register(name: String, texture: Texture) {
        textureSet[name] = texture
    }

    fun unRegister(name: String) {
        textureSet.remove(name)
    }


    override fun close() {
        mShaderLibrary.close()
        textureSet.forEach() {
            it.value.close()
        }
    }

}