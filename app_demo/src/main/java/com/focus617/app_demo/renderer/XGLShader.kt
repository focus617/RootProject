package com.focus617.app_demo.renderer

import android.content.Context
import android.opengl.GLES31.*
import com.focus617.core.engine.renderer.Shader
import com.focus617.platform.helper.FileHelper

open class XGLShader constructor(
    vertexShader: String,
    fragmentShader: String
) : Shader(vertexShader, fragmentShader) {

    /** 基于Resource/raw中的文件构造 */
    constructor(
        context: Context,
        vertexShaderResourceId: Int,
        fragmentShaderResourceId: Int
    ) : this(
        FileHelper.loadFromResourceFile(context, vertexShaderResourceId),
        FileHelper.loadFromResourceFile(context, fragmentShaderResourceId)
    )

    /** 基于Assets中的文件构造 */
    constructor(
        context: Context,
        path: String,
        vertexShaderFileName: String,
        fragmentShaderFileName: String
    ) : this(
        FileHelper.loadFromAssetsFile(context, "$path/$vertexShaderFileName"),
        FileHelper.loadFromAssetsFile(context, "$path/$fragmentShaderFileName")
    )

    override var mRendererId: Int = buildProgram(vertexShader, fragmentShader)

    override fun bind() {
        glUseProgram(mRendererId)
    }

    override fun unbind() {
        glUseProgram(0)
    }

    override fun close() {
        glDeleteProgram(mRendererId)
    }

    companion object {
        val TAG = "XGLShader"

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
                    "$TAG: shader compilation result: ${compileStatus[0]}" +
                            glGetShaderInfoLog(shaderObjectId)
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
                            + "Log:${glGetProgramInfoLog(programObjectId)}").trimIndent()
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
                            + "Log:${glGetProgramInfoLog(programObjectId)}").trimIndent()
                )
                return false
            }

            LOG.debug("$TAG: Shader validation success.")
            return true
        }
    }


}