package com.focus617.core.engine.renderer.shader

import com.focus617.core.engine.math.*
import com.focus617.core.engine.resource.baseDataType.Color
import com.focus617.core.platform.base.BaseEntity
import java.io.Closeable

/**
 * 着色器类 Shader
 * 1. 储存了着色器程序的句柄 [mHandle]
 * 2. 它的构造器需要顶点着色器和片段着色器的源代码
 */
abstract class Shader(
    name: String,
    vertexShader: String,
    fragmentShader: String
) : BaseEntity(), Closeable {

    protected abstract var mHandle: Int
    protected var nName: String = name

    fun getName() = nName

    abstract fun bind()
    abstract fun unbind()

    abstract fun setInt(name: String, value: Int)
    abstract fun setIntArray(name: String, values: IntArray, count: Int)
    abstract fun setFloat(name: String, value: Float)
    abstract fun setFloat2(name: String, value: Vector2)
    abstract fun setFloat3(name: String, value: Vector3)
    abstract fun setFloat3(name: String, value: Point3D)
    abstract fun setFloat4(name: String, value: Vector4)
    abstract fun setFloat4(name: String, value: Color)
    abstract fun setMat4(name: String, matrix: FloatArray)
    abstract fun setMat4(name: String, matrix: Mat4)
}
