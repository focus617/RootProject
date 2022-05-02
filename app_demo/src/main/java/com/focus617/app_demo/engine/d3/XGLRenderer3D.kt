package com.focus617.app_demo.engine.d3

import android.content.Context
import android.opengl.GLSurfaceView
import com.focus617.app_demo.engine.XGLContext
import com.focus617.app_demo.renderer.XGLShader
import com.focus617.app_demo.renderer.XGLShaderBuilder
import com.focus617.app_demo.renderer.XGLTextureCubeMap
import com.focus617.app_demo.renderer.XGLVertexArray
import com.focus617.core.engine.baseDataType.Color
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.objects.DrawableObject
import com.focus617.core.engine.objects.IfDrawable
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.VertexArray
import com.focus617.core.engine.renderer.XRenderer
import com.focus617.core.engine.scene.Camera
import com.focus617.core.engine.scene.PerspectiveCamera
import com.focus617.core.engine.scene.PerspectiveCameraController
import com.focus617.core.engine.scene.Scene
import java.io.Closeable
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class XGLRenderer3D(
    private val context: Context,
    private val scene: Scene
) : XRenderer(), GLSurfaceView.Renderer, Closeable {

    //TODO: Game objects should NOT owned by Renderer .
    // It should be injected from Engine's Scene, since GlSurfaceView/Renderer is always recreated
    // in case of configuration change, etc.
    override val mCameraController =
        PerspectiveCameraController(scene.mCamera as PerspectiveCamera)

    private lateinit var sVertexArray: XGLVertexArray

    override fun initRenderer() {
        RenderCommand.init()
        initTextureForScene()
        initShaderForScene(context)     // 初始化本Render的静态数据

        initVertexArray(scene.gameObjectList[0])
    }

    private fun initTextureForScene() {
        val texture = XGLTextureCubeMap(
            context,
            SHADER_PATH,
            arrayOf("left.png", "right.png", "bottom.png", "top.png", "front.png", "back.png")
        )
        scene.register(objectTextureName, texture)
    }

    private fun initShaderForScene(context: Context) {
        // TODO: How to create objects in Sandbox layer?
        val shader = XGLShaderBuilder.createShader(
            context,
            "$SHADER_PATH/$SHADER_FILE"
        ) as XGLShader
        shader.bind()
        shader.setInt("u_TextureUnit", 0)
        scene.mShaderLibrary.add(shader)
    }

    private fun initVertexArray(drawingObject: IfDrawable) {
        sVertexArray = XGLVertexArray.buildVertexArray(drawingObject)
    }

    override fun close() {
    }

    override fun beginScene(camera: Camera) {
        // 在多线程渲染里，会把BeginScene函数放在RenderCommandQueue里执行
        // camera在多线程渲染的时候不能保证主线程是否正在更改Camera的相关信息
        synchronized(camera) {
            System.arraycopy(camera.getProjectionMatrix(), 0, SceneData.sProjectionMatrix, 0, 16)
            System.arraycopy(camera.getViewMatrix(), 0, SceneData.sViewMatrix, 0, 16)
        }

        val shader = scene.mShaderLibrary.get(SHADER_FILE)
        shader?.bind()
//        LOG.info(XMatrix.toString(SceneData.sProjectionMatrix, matrixName = "ProjectionMatrix"))
//        LOG.info(XMatrix.toString(SceneData.sViewMatrix, matrixName = "ViewMatrix"))

        shader?.setMat4("u_ProjectionMatrix", SceneData.sProjectionMatrix)
        shader?.setMat4("u_ViewMatrix", SceneData.sViewMatrix)
    }

    override fun endScene() {

    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // 打印OpenGL Version，Vendor，etc
        XGLContext.getOpenGLInfo()

        // 设置重绘背景框架颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1F))
        RenderCommand.clear()

        // TODO: 当前的问题是，必须在opengl线程才能调用opengl api，无法在主线程调用。
        // 调用XRenderer.init, 因为涉及opengl api, 只好在这里调用
        this.initRenderer()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        LOG.info("onSurfaceChanged (width = $width, height = $height)")

        // 设置渲染的OpenGL场景（视口）的位置和大小
        RenderCommand.setViewport(0, 0, width, height)

        mCameraController.onWindowSizeChange(width, height)
    }

    override fun onDrawFrame(unused: GL10) {
        // 清理屏幕，重绘背景颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1F))
        RenderCommand.clear()

        beginScene(scene.mCamera)

        var transform = transformGameObject(
            scene.gameObjectList[0],
            Vector3(0.0f, 0.0f, 0.0f),
            Vector2(1f, 1f)
        )
        //LOG.info(XMatrix.toString(transform, matrixName = "ModelMatrix"))

        scene.texture(objectTextureName)!!.bind()
        submit(sVertexArray, transform)

        // This texture has transparent alpha for part of image
//        (scene.texture(logoTextureName)!! as Texture2D).bind()
//        submit(sVertexArray, transform)

        endScene()
    }

    private fun transformGameObject(
        gameObject: DrawableObject,
        position: Vector3,
        size: Vector2,
        rotation: Float = 0.0f
    ): FloatArray {
        gameObject.resetTransform()
        gameObject.onTransform(position, size, rotation)
        return gameObject.modelMatrix
    }

    private fun submit(
        vertexArray: VertexArray,
        transform: FloatArray
    ) {
        val shader = scene.mShaderLibrary.get(SHADER_FILE)
        (shader as XGLShader).bind()
        shader.setMat4("u_ModelMatrix", transform)
        //shader.setFloat4("u_Color", WHITE)

        vertexArray.bind()
        RenderCommand.drawIndexed(vertexArray)

        // 下面这两行可以省略，以节约GPU的运行资源；
        // 在下个submit，会bind其它handle，自然会实现unbind
        vertexArray.unbind()
        shader.unbind()
    }


    companion object {
        private val SHADER_PATH = "Cube"
        private val SHADER_FILE = "SkyBox.glsl"

        private val objectTextureName = "SkyCubeMapTexture"

    }

}