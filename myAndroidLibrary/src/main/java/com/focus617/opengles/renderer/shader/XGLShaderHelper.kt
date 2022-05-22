package com.focus617.opengles.renderer.shader

import android.content.Context
import android.opengl.GLES31.*
import android.text.TextUtils
import com.focus617.core.platform.base.BaseEntity
import timber.log.Timber
import java.util.*

object XGLShaderHelper: BaseEntity() {
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

        Timber.i("$TAG: parse shader source: $filePath")

        shaderSources.clear()
        mode = ShaderType.None

        if (filePath.isEmpty() or TextUtils.isEmpty(filePath)) {
            Timber.e("Shader file doesn't exist")
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
            Timber.e(e.message.toString())
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

        Timber.e("$TAG: buildProgram()")

        val program: Int

        // Compile the Vertex shaders.
        val vertexShader = compileVertexShader(vertexShaderSource)
        if (vertexShader == GL_FALSE) {
            Timber.e("$TAG: Vertex shader compilation failure.")
            return GL_FALSE
        }
        // Compile the Fragment shaders.
        val fragmentShader = compileFragmentShader(fragmentShaderSource)
        if (fragmentShader == GL_FALSE) {
            Timber.e("$TAG: Fragment shader compilation failure.")
            glDeleteShader(vertexShader)    // Don't leak shaders.
            return GL_FALSE
        }

        // Vertex and fragment shaders are successfully compiled.
        // Now time to link them together into a program.
        program = linkProgram(vertexShader, fragmentShader)
        if (program == GL_FALSE) {
            Timber.e("$TAG: Shader link failure!")
            // 销毁不再需要的着色器对象
            glDeleteShader(vertexShader)
            glDeleteShader(fragmentShader)
            return GL_FALSE
        }

        if (validateProgram(program))
            Timber.d("$TAG buildProgram(): Program=$program")

        // Always detach shaders after a successful link.
        glDetachShader(program, vertexShader)
        glDetachShader(program, fragmentShader)
        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)

        // 释放着色器编译器使用的资源
        glReleaseShaderCompiler()

        return program
    }

    /**
     * Loads and compiles a vertex shader, returning the OpenGL object ID.
     */
    private fun compileVertexShader(shaderCode: String): Int {
        Timber.d("$TAG: compileVertexShader()")
        return compileShader(GL_VERTEX_SHADER, shaderCode)
    }

    /**
     * Loads and compiles a fragment shader, returning the OpenGL object ID.
     */
    private fun compileFragmentShader(shaderCode: String): Int {
        Timber.d("$TAG: compileFragmentShader()")
        return compileShader(GL_FRAGMENT_SHADER, shaderCode)
    }

    /**
     * Compiles a shader, returning the OpenGL object ID.
     */
    private fun compileShader(type: Int, shaderCode: String): Int {
        // Create a new shader object.
        val shaderObjectId = glCreateShader(type)
        if (shaderObjectId == GL_FALSE) {
            Timber.e("$TAG: compileShader(): Could not create new shader.")
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
            Timber.e("$TAG: Shader compilation failure.")
            Timber.e(
                ("$TAG: shader compilation result: ${compileStatus[0]}" +
                        "Log: ${glGetShaderInfoLog(shaderObjectId)}").trimIndent()
            )

            // If it failed, delete the shader object.
            glDeleteShader(shaderObjectId)

            return GL_FALSE
        }

        Timber.d("$TAG: Shader compilation success.")
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
            Timber.e("$TAG: Could not create new program")
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
            Timber.e("$TAG: Shader link failure!")
            // Print the program info log to the Android log output.
            Timber.e(
                ("$TAG: shader linking result:${linkStatus[0]}\n"
                        + "Log: ${glGetProgramInfoLog(programObjectId)}").trimIndent()
            )

            // If it failed, delete the program object.
            glDeleteProgram(programObjectId)

            return GL_FALSE
        }
        Timber.d("$TAG: Shader linking success.")
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
            Timber.e("$TAG: Shader validation failure!")
            Timber.e(
                ("$TAG: shader validation result: ${validateStatus[0]}\n"
                        + "Log: ${glGetProgramInfoLog(programObjectId)}").trimIndent()
            )
            return false
        }

        Timber.d("$TAG: Shader validation success.")
        return true
    }
}
