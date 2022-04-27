package com.focus617.app_demo.renderer

import android.content.Context
import android.opengl.GLES31.*
import android.text.TextUtils
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.Vector4
import com.focus617.core.engine.renderer.Shader
import com.focus617.platform.helper.FileHelper
import java.util.*

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

    override var mHandle: Int = buildProgram(vertexShaderSrc, fragmentShaderSrc)

    override fun bind() {
        glUseProgram(mHandle)
    }

    override fun unbind() {
        glUseProgram(0)
    }

    override fun close() {
        glDeleteProgram(mHandle)
    }

    private val mLocationCache = HashMap<String, Int>()

    private fun getLocation(name: String): Int =
        if (mLocationCache.containsKey(name)) mLocationCache[name]!!
        else {
            val location = glGetUniformLocation(mHandle, name)
            mLocationCache[name] = location
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

fun uploadUniformFloat(name: String, value: Float) {
    glUniform1f(getLocation(name), value)
}

fun uploadUniformFloat2(name: String, value: Vector2) {
    glUniform2f(getLocation(name), value.x, value.y)
}

fun uploadUniformFloat3(name: String, value: Vector3) {
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

companion object {
    val TAG = "XGLShader"

    enum class ShaderType {
        None,
        VERTEX_SHADER,
        FRAGMENT_SHADER
    }

    var mode: ShaderType = ShaderType.None
    var name: String = ""
    val shaderSources = HashMap<ShaderType, String>()

    fun parseShaderSource(context: Context, filePath: String) {
        val typeToken = "#type"

        LOG.info("$TAG: parse shader source: $filePath")

        shaderSources.clear()

        if (filePath.isEmpty() or TextUtils.isEmpty(filePath)) {
            LOG.error("Shader file doesn't exist")
        }
        name = FileHelper.getFileName(filePath)

        try {
            val scanner = Scanner(context.assets.open(filePath))
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine().trim()

                when {
                    line.startsWith(typeToken) -> switchMode(line)
                    else -> appendLine(line)
                }
            }
            scanner.close()
        } catch (e: Exception) {
            LOG.error(e.message.toString())
        }
    }

    private fun switchMode(line: String) {
        val DELIMITER = Regex("[ ]+")    // 分隔符
        val VERTEX = "VERTEX"
        val FRAGMENT = "FRAGMENT"

        val items = line.split(DELIMITER).toTypedArray()
        if (items.size != 2) return
        mode = when (items[1].uppercase(Locale.getDefault())) {
            VERTEX -> ShaderType.VERTEX_SHADER
            FRAGMENT -> ShaderType.FRAGMENT_SHADER
            else -> ShaderType.None
        }
    }

    private fun appendLine(line: String) {
        val str = shaderSources[mode] ?: StringBuilder().toString()
        shaderSources[mode] = StringBuilder(str).append(line + '\n').toString()
    }

    /**
     * Helper function that compiles the shaders, links and validates the
     * program, returning the program ID.
     */
    fun buildProgram(
        vertexShaderSource: String,
        fragmentShaderSource: String
    ): Int {

        LOG.debug("$TAG: buildProgram()")

        val program: Int

        // Compile the Vertex shaders.
        val vertexShader = compileVertexShader(vertexShaderSource)
        if (vertexShader == GL_FALSE) {
            LOG.error("$TAG: Vertex shader compilation failure.")
            return GL_FALSE
        }
        // Compile the Fragment shaders.
        val fragmentShader = compileFragmentShader(fragmentShaderSource)
        if (fragmentShader == GL_FALSE) {
            LOG.error("$TAG: Fragment shader compilation failure.")
            glDeleteShader(vertexShader)    // Don't leak shaders.
            return GL_FALSE
        }

        // Vertex and fragment shaders are successfully compiled.
        // Now time to link them together into a program.
        program = linkProgram(vertexShader, fragmentShader)
        if (program == GL_FALSE) {
            LOG.error("$TAG: Shader link failure!")
            // 销毁不再需要的着色器对象
            glDeleteShader(vertexShader)
            glDeleteShader(fragmentShader)
            return GL_FALSE
        }

        if (validateProgram(program))
            LOG.debug("$TAG buildProgram(): Program=$program")

        // Always detach shaders after a successful link.
        glDetachShader(program, vertexShader)
        glDetachShader(program, fragmentShader)

        // 释放着色器编译器使用的资源
        glReleaseShaderCompiler()

        return program
    }

    /**
     * Loads and compiles a vertex shader, returning the OpenGL object ID.
     */
    private fun compileVertexShader(shaderCode: String): Int {
        LOG.debug("$TAG: compileVertexShader()")
        return compileShader(GL_VERTEX_SHADER, shaderCode)
    }

    /**
     * Loads and compiles a fragment shader, returning the OpenGL object ID.
     */
    private fun compileFragmentShader(shaderCode: String): Int {
        LOG.debug("$TAG: compileFragmentShader()")
        return compileShader(GL_FRAGMENT_SHADER, shaderCode)
    }

    /**
     * Compiles a shader, returning the OpenGL object ID.
     */
    private fun compileShader(type: Int, shaderCode: String): Int {
        // Create a new shader object.
        val shaderObjectId = glCreateShader(type)
        if (shaderObjectId == GL_FALSE) {
            LOG.error("$TAG: compileShader(): Could not create new shader.")
            return GL_FALSE
        }

        // Pass in the shader source code to GL
        glShaderSource(shaderObjectId, shaderCode)

        // Compile the shader.
        glCompileShader(shaderObjectId)

        // Get the compilation status.
        val compileStatus = IntArray(1)
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0)

        // Verify the compile status.
        if (compileStatus[0] == GL_FALSE) {
            // Print the shader info log to the Android log output.
            LOG.error("$TAG: Shader compilation failure.")
            LOG.error(
                ("$TAG: shader compilation result: ${compileStatus[0]}" +
                        "Log: ${glGetShaderInfoLog(shaderObjectId)}").trimIndent()
            )

            // If it failed, delete the shader object.
            glDeleteShader(shaderObjectId)

            return GL_FALSE
        }

        LOG.debug("$TAG: Shader compilation success.")
        // Return the shader object ID.
        return shaderObjectId
    }

    /**
     * Links a vertex shader and a fragment shader together into an OpenGL
     * program. Returns the OpenGL program object ID, or 0 if linking failed.
     */
    private fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
        // Create a new program object.
        val programObjectId = glCreateProgram()
        if (programObjectId == GL_FALSE) {
            LOG.error("$TAG: Could not create new program")
            return GL_FALSE
        }

        // Attach the vertex shader to the program.
        glAttachShader(programObjectId, vertexShaderId)

        // Attach the fragment shader to the program.
        glAttachShader(programObjectId, fragmentShaderId)

        // Link the two shaders together into a program.
        glLinkProgram(programObjectId)

        // Get the link status.
        val linkStatus = IntArray(1)
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0)

        // Verify the link status.
        if (linkStatus[0] == GL_FALSE) {
            LOG.error("$TAG: Shader link failure!")
            // Print the program info log to the Android log output.
            LOG.error(
                ("$TAG: shader linking result:${linkStatus[0]}\n"
                        + "Log: ${glGetProgramInfoLog(programObjectId)}").trimIndent()
            )

            // If it failed, delete the program object.
            glDeleteProgram(programObjectId)

            return GL_FALSE
        }
        LOG.debug("$TAG: Shader linking success.")
        // Return the program object ID.
        return programObjectId
    }

    /**
     * Validates an OpenGL program. Should only be called when developing the
     * application.
     */
    private fun validateProgram(programObjectId: Int): Boolean {
        glValidateProgram(programObjectId)

        val validateStatus = IntArray(1)
        glGetProgramiv(
            programObjectId, GL_VALIDATE_STATUS,
            validateStatus, 0
        )
        if (validateStatus[0] == GL_FALSE) {
            LOG.error("$TAG: Shader validation failure!")
            LOG.error(
                ("$TAG: shader validation result: ${validateStatus[0]}\n"
                        + "Log: ${glGetProgramInfoLog(programObjectId)}").trimIndent()
            )
            return false
        }

        LOG.debug("$TAG: Shader validation success.")
        return true
    }
}


}