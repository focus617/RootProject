package com.focus617.opengles.text


import com.focus617.core.engine.ecs.mine.static.SceneData
import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.engine.mesh.IfMeshable
import com.focus617.core.engine.mesh.Mesh
import com.focus617.core.engine.renderer.Material
import com.focus617.core.engine.renderer.api.ShaderUniformConstants.U_PROJECT_MATRIX
import com.focus617.core.engine.renderer.api.ShaderUniformConstants.U_VIEW_MATRIX
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.renderer.vertex.BufferElement
import com.focus617.core.engine.renderer.vertex.BufferLayout
import com.focus617.core.engine.renderer.vertex.ShaderDataType
import com.focus617.core.engine.resource.baseDataType.Color
import com.focus617.core.engine.scene_graph.GeometryEntity
import com.focus617.core.engine.scene_graph.components.MeshRenderer
import com.focus617.opengles.renderer.texture.XGLTexture2D
import com.focus617.opengles.renderer.texture.XGLTextureSlots
import com.focus617.opengles.renderer.vertex.XGLVertexArray
import com.focus617.opengles.scene_graph.XGLDrawableObject

/**
 *  * Two kinds of objects:
 * 1. Perspective 3D Text, textQuad1 is example
 * 2. Orthographic 3D Text, which projection matrix is orthographic(no zoom)
 *    textQuad2 is example for this type.
 */
class TextEntity3D(private val isPerspective: Boolean = true) : GeometryEntity(),
    XGLDrawableObject {
    private lateinit var textTexture: XGLTexture2D
    private var textureIndex: Int = -1    // 在TextureSlots内的Index

    var text: String = "Hello World!"
    var textColor: Color = Color.WHITE
    var textFont: Float = 100f

    private var preText: String = text
    private var preTextFont: Float = textFont

    init {
        shaderName = ShaderFilePath
    }

    override fun initOpenGlResource() {
        textTexture = XGLTexture2D("TextTexture")
        textTexture.setText(text, textFont)
        //TODO: Why below routine will cause abnormal
//        textureIndex = XGLTextureSlots.requestIndex(textTexture)

        val mesh = Mesh(XGLVertexArray.buildVertexArray(TextQuad3D()))
        val meshRenderer = MeshRenderer(mesh, Material())
        addComponent(meshRenderer)
    }

    override fun onRender(shader: Shader) {
        if ((text != preText) || (textFont != preTextFont)) {
            textTexture.setText(text, textFont)

            preText = text
            preTextFont = textFont
        }
        // 注册到TextureSlots, 获得TextureUnit Index, 以便ActiveTexture
        textureIndex = XGLTextureSlots.requestIndex(textTexture)
//        Timber.i("onRender: use texture index=$textureIndex")
        textTexture.bind(textureIndex)

        if (!isPerspective) {
            shader.setMat4(U_PROJECT_MATRIX, mProjectionMatrix)
            shader.setMat4(U_VIEW_MATRIX, SceneData.sViewMatrix)
        }

        shader.setInt(U_TEXTURE, textureIndex)
        shader.setFloat4(U_COLOR, textColor)

        super.onRender(shader)
    }

    companion object {
        const val SHADER_PATH = "Text"
        const val SHADER_FILE = "shader.glsl"
        const val ShaderFilePath: String = "$SHADER_PATH/$SHADER_FILE"
        const val U_TEXTURE = "u_Texture"
        const val U_COLOR = "u_Color"

        lateinit var shader: Shader


        private val mProjectionMatrix = Mat4()
        var mZoomLevel: Float = 1.0f
        fun onWindowSizeChange(width: Int, height: Int) {
            val matrix = FloatArray(16)
            // 计算正交投影矩阵 (Project Matrix)
            // 默认绘制的区间在横轴[-1.7778f, 1.778f]，纵轴[-1, 1]之间
            if (width > height) {
                // Landscape
                val aspect: Float = width.toFloat() / height.toFloat()
                val ratio = aspect * mZoomLevel

                // 用ZoomLevel来表示top，因为拉近镜头时，ZoomLevel变大，而对应可见区域会变小
                XMatrix.orthoM(
                    matrix,
                    0,
                    -ratio,
                    ratio,
                    -mZoomLevel,
                    mZoomLevel,
                    -0.1f,
                    10f
                )
            } else {
                // Portrait or Square
                val aspect: Float = height.toFloat() / width.toFloat()
                val ratio = aspect * mZoomLevel
                XMatrix.orthoM(
                    matrix,
                    0,
                    -mZoomLevel,
                    mZoomLevel,
                    -ratio,
                    ratio,
                    -0.1f,
                    10f
                )
            }
            mProjectionMatrix.setValue(matrix)
        }
    }

    class TextQuad3D : IfMeshable {
        override fun getVertices(): FloatArray = floatArrayOf(
            // Vertex attributes for a quad that fills the entire screen in Normalized Device Coordinates.
            // 每个顶点有2个顶点属性一位置、纹理
            // 在OpenGLES3.0采用的坐标系统中，纹理坐标的原点是纹理图的左上角
            // x,   y,  TextureX, TextureY
            -0.5f, 0.5f, 0.0f, 0.0f,  //0 左上
            -0.5f, -0.5f, 0.0f, 1.0f,  //1 左下
            0.5f, -0.5f, 1.0f, 1.0f,   //2 右下
            0.5f, 0.5f, 1.0f, 0.0f    //3 右上
        )

        override fun getLayout(): BufferLayout = BufferLayout(
            listOf(
                BufferElement("a_Position", ShaderDataType.Float2, true),
                BufferElement("a_TexCoords", ShaderDataType.Float2, true)
            )
        )

        override fun getIndices(): ShortArray = shortArrayOf(
            0, 1, 2, 0, 2, 3
        )

        override fun beforeBuild() {
        }

        override fun afterBuild() {
        }
    }

}