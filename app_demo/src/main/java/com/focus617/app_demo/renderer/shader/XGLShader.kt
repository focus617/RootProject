package com.focus617.app_demo.renderer.shader

import android.content.Context
import android.opengl.GLES31.*
import com.focus617.core.engine.math.*
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.resource.baseDataType.Color
import com.focus617.platform.helper.FileHelper
import java.nio.IntBuffer

/**
 * OpenGL纹理类 XGLShader
 * 1. 储存了着色器程序的句柄 [mHandle]
 * 2. 它的构造器需要顶点着色器和片段着色器的源代码
 */
class XGLShader constructor(
    name: String,
    vertexShaderSrc: String,
    fragmentShaderSrc: String
) : Shader(name, vertexShaderSrc, fragmentShaderSrc) {

    /** 基于Resource/raw中的文件构造 */
    constructor(
        context: Context,
        name: String,
        vertexShaderResourceId: Int,
        fragmentShaderResourceId: Int
    ) : this(
        name,
        FileHelper.loadFromResourceFile(context, vertexShaderResourceId),
        FileHelper.loadFromResourceFile(context, fragmentShaderResourceId)
    )

    /** 基于Assets中的文件构造 */
    constructor(
        context: Context,
        name: String,
        path: String,
        vertexShaderFileName: String,
        fragmentShaderFileName: String
    ) : this(
        name,
        FileHelper.loadFromAssetsFile(context, "$path/$vertexShaderFileName"),
        FileHelper.loadFromAssetsFile(context, "$path/$fragmentShaderFileName")
    )

    override var mHandle: Int = XGLShaderHelper.buildProgram(vertexShaderSrc, fragmentShaderSrc)

    override fun bind() {
        glUseProgram(mHandle)
    }

    override fun unbind() {
        glUseProgram(0)
    }

    override fun setInt(name: String, value: Int) {
        uploadUniformInt(name, value)
    }

    override fun setIntArray(name: String, values: IntArray, count: Int) {
        uploadUniformIntArray(name, values, count)
    }

    override fun setFloat(name: String, value: Float) {
        uploadUniformFloat(name, value)
    }

    override fun setFloat3(name: String, value: Vector3) {
        uploadUniformFloat3(name, value)
    }

    override fun setFloat3(name: String, value: Point3D) {
        uploadUniformFloat3(name, value)
    }

    override fun setFloat4(name: String, value: Vector4) {
        uploadUniformFloat4(name, value)
    }

    override fun setFloat4(name: String, value: Color) {
        uploadUniformFloat4(name, value.toVector4())
    }

    override fun setMat4(name: String, matrix: FloatArray) {
        uploadUniformMat4(name, matrix)
    }

    override fun setMat4(name: String, matrix: Mat4) {
        uploadUniformMat4(name, matrix.toFloatArray())
    }

    override fun close() {
        glDeleteProgram(mHandle)
    }

    private val mLocationCache = HashMap<String, Int>()

    private fun getLocation(uniformName: String): Int =
        if (mLocationCache.containsKey(uniformName)) mLocationCache[uniformName]!!
        else {
            val location = glGetUniformLocation(mHandle, uniformName)
            assert(location != -1){
                "Could not find location of a uniform($uniformName) in shader($nName)"
            }
            mLocationCache[uniformName] = location
            location
        }

    /**
     * uniform工具函数
     * 所有的函数能够查询一个uniform的位置句柄，并设置它的值。
     */
    fun uploadUniformBool(name: String, bool: Boolean) {
        val value = if (bool) 1 else 0
        glUniform1i(getLocation(name), value)
    }

    fun uploadUniformInt(name: String, value: Int) {
        glUniform1i(getLocation(name), value)
    }

    fun uploadUniformIntArray(name: String, values: IntArray, count: Int) {
        glUniform1iv(getLocation(name), count, IntBuffer.wrap(values))
    }

    fun uploadUniformFloat(name: String, value: Float) {
        glUniform1f(getLocation(name), value)
    }

    fun uploadUniformFloat2(name: String, value: Vector2) {
        glUniform2f(getLocation(name), value.x, value.y)
    }

    fun uploadUniformFloat3(name: String, value: Vector3) {
        glUniform3f(getLocation(name), value.x, value.y, value.z)
    }

    fun uploadUniformFloat3(name: String, value: Point3D) {
        glUniform3f(getLocation(name), value.x, value.y, value.z)
    }

    fun uploadUniformFloat4(name: String, value: Vector4) {
        glUniform4f(getLocation(name), value.x, value.y, value.z, value.w)
    }

    fun uploadUniformMat3(name: String, matrix: FloatArray) {
        glUniformMatrix3fv(getLocation(name), 1, false, matrix, 0)
    }

    fun uploadUniformMat4(name: String, matrix: FloatArray) {
        glUniformMatrix4fv(getLocation(name), 1, false, matrix, 0)
    }

    fun uploadUniformTexture(name: String, value: Int) {
        uploadUniformInt(name, value)
    }
}