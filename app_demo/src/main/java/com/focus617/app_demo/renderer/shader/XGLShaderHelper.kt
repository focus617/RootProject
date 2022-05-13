package com.focus617.app_demo.renderer.shader

import android.content.Context
import android.opengl.GLES31
import android.text.TextUtils
import com.focus617.core.platform.base.BaseEntity
import java.util.*

object XGLShaderHelper {
    val TAG = "XGLShader"

    enum class ShaderType {
        None,
        VERTEX_SHADER,
        FRAGMENT_SHADER
    }

    var mode: ShaderType = ShaderType.None
    var name: String = ""
    val shaderSources = HashMap<ShaderType, String>()

    /**
     * Helper function that compiles the single shaders with both vertex and fragment shaders
     */
    fun parseShaderSource(context: Context, filePath: String) {
        val typeToken = "#type"

        BaseEntity.LOG.info("$TAG: parse shader source: $filePath")

        shaderSources.clear()
        mode = ShaderType.None

        if (filePath.isEmpty() or TextUtils.isEmpty(filePath)) {
            BaseEntity.LOG.error("Shader file doesn't exist")
        }
        name = filePath

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
            BaseEntity.LOG.error(e.message.toString())
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
     * Helper function that compiles the shaders, links and validates the program,
     * returning the program ID.
     */
    fun buildProgram(
        vertexShaderSource: String,
        fragmentShaderSource: String
    ): Int {

        BaseEntity.LOG.debug("$TAG: buildProgram()")

        val program: Int

        // Compile the Vertex shaders.
        val vertexShader = compileVertexShader(vertexShaderSource)
        if (vertexShader == GLES31.GL_FALSE) {
            BaseEntity.LOG.error("$TAG: Vertex shader compilation failure.")
            return GLES31.GL_FALSE
        }
        // Compile the Fragment shaders.
        val fragmentShader = compileFragmentShader(fragmentShaderSource)
        if (fragmentShader == GLES31.GL_FALSE) {
            BaseEntity.LOG.error("$TAG: Fragment shader compilation failure.")
            GLES31.glDeleteShader(vertexShader)    // Don't leak shaders.
            return GLES31.GL_FALSE
        }

        // Vertex and fragment shaders are successfully compiled.
        // Now time to link them together into a program.
        program = linkProgram(vertexShader, fragmentShader)
        if (program == GLES31.GL_FALSE) {
            BaseEntity.LOG.error("$TAG: Shader link failure!")
            // 销毁不再需要的着色器对象
            GLES31.glDeleteShader(vertexShader)
            GLES31.glDeleteShader(fragmentShader)
            return GLES31.GL_FALSE
        }

        if (validateProgram(program))
            BaseEntity.LOG.debug("$TAG buildProgram(): Program=$program")

        // Always detach shaders after a successful link.
        GLES31.glDetachShader(program, vertexShader)
        GLES31.glDetachShader(program, fragmentShader)
        GLES31.glDeleteShader(vertexShader)
        GLES31.glDeleteShader(fragmentShader)

        // 释放着色器编译器使用的资源
        GLES31.glReleaseShaderCompiler()

        return program
    }

    /**
     * Loads and compiles a vertex shader, returning the OpenGL object ID.
     */
    private fun compileVertexShader(shaderCode: String): Int {
        BaseEntity.LOG.debug("$TAG: compileVertexShader()")
        return compileShader(GLES31.GL_VERTEX_SHADER, shaderCode)
    }

    /**
     * Loads and compiles a fragment shader, returning the OpenGL object ID.
     */
    private fun compileFragmentShader(shaderCode: String): Int {
        BaseEntity.LOG.debug("$TAG: compileFragmentShader()")
        return compileShader(GLES31.GL_FRAGMENT_SHADER, shaderCode)
    }

    /**
     * Compiles a shader, returning the OpenGL object ID.
     */
    private fun compileShader(type: Int, shaderCode: String): Int {
        // Create a new shader object.
        val shaderObjectId = GLES31.glCreateShader(type)
        if (shaderObjectId == GLES31.GL_FALSE) {
            BaseEntity.LOG.error("$TAG: compileShader(): Could not create new shader.")
            return GLES31.GL_FALSE
        }

        // Pass in the shader source code to GL
        GLES31.glShaderSource(shaderObjectId, shaderCode)

        // Compile the shader.
        GLES31.glCompileShader(shaderObjectId)

        // Get the compilation status.
        val compileStatus = IntArray(1)
        GLES31.glGetShaderiv(shaderObjectId, GLES31.GL_COMPILE_STATUS, compileStatus, 0)

        // Verify the compile status.
        if (compileStatus[0] == GLES31.GL_FALSE) {
            // Print the shader info log to the Android log output.
            BaseEntity.LOG.error("$TAG: Shader compilation failure.")
            BaseEntity.LOG.error(
                ("$TAG: shader compilation result: ${compileStatus[0]}" +
                        "Log: ${GLES31.glGetShaderInfoLog(shaderObjectId)}").trimIndent()
            )

            // If it failed, delete the shader object.
            GLES31.glDeleteShader(shaderObjectId)

            return GLES31.GL_FALSE
        }

        BaseEntity.LOG.debug("$TAG: Shader compilation success.")
        // Return the shader object ID.
        return shaderObjectId
    }

    /**
     * Links a vertex shader and a fragment shader together into an OpenGL
     * program. Returns the OpenGL program object ID, or 0 if linking failed.
     */
    private fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
        // Create a new program object.
        val programObjectId = GLES31.glCreateProgram()
        if (programObjectId == GLES31.GL_FALSE) {
            BaseEntity.LOG.error("$TAG: Could not create new program")
            return GLES31.GL_FALSE
        }

        // Attach the vertex shader to the program.
        GLES31.glAttachShader(programObjectId, vertexShaderId)

        // Attach the fragment shader to the program.
        GLES31.glAttachShader(programObjectId, fragmentShaderId)

        // Link the two shaders together into a program.
        GLES31.glLinkProgram(programObjectId)

        // Get the link status.
        val linkStatus = IntArray(1)
        GLES31.glGetProgramiv(programObjectId, GLES31.GL_LINK_STATUS, linkStatus, 0)

        // Verify the link status.
        if (linkStatus[0] == GLES31.GL_FALSE) {
            BaseEntity.LOG.error("$TAG: Shader link failure!")
            // Print the program info log to the Android log output.
            BaseEntity.LOG.error(
                ("$TAG: shader linking result:${linkStatus[0]}\n"
                        + "Log: ${GLES31.glGetProgramInfoLog(programObjectId)}").trimIndent()
            )

            // If it failed, delete the program object.
            GLES31.glDeleteProgram(programObjectId)

            return GLES31.GL_FALSE
        }
        BaseEntity.LOG.debug("$TAG: Shader linking success.")
        // Return the program object ID.
        return programObjectId
    }

    /**
     * Validates an OpenGL program. Should only be called when developing the
     * application.
     */
    private fun validateProgram(programObjectId: Int): Boolean {
        GLES31.glValidateProgram(programObjectId)

        val validateStatus = IntArray(1)
        GLES31.glGetProgramiv(
            programObjectId, GLES31.GL_VALIDATE_STATUS,
            validateStatus, 0
        )
        if (validateStatus[0] == GLES31.GL_FALSE) {
            BaseEntity.LOG.error("$TAG: Shader validation failure!")
            BaseEntity.LOG.error(
                ("$TAG: shader validation result: ${validateStatus[0]}\n"
                        + "Log: ${GLES31.glGetProgramInfoLog(programObjectId)}").trimIndent()
            )
            return false
        }

        BaseEntity.LOG.debug("$TAG: Shader validation success.")
        return true
    }
}
