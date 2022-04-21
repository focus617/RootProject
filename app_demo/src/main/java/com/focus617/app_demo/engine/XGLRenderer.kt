package com.focus617.app_demo.engine

import android.opengl.GLES30
import android.opengl.GLES31
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import com.focus617.app_demo.objects.Triangle
import com.focus617.core.engine.core.IfWindow
import timber.log.Timber
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class XGLRenderer(private val window: IfWindow) : GLSurfaceView.Renderer {

    private val mMVPMatrix = FloatArray(16)

    private val mProjectionMatrix = FloatArray(16)

    private val mViewMatrix = FloatArray(16)

    private lateinit var mTriangle: Triangle

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // 打印OpenGL Version，Vendor，etc
        ((window as AndroidWindow).renderContext as XGLContext).getOpenGLInfo()

        // 设置重绘背景框架颜色
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        mTriangle = Triangle()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        Timber.d("width = $width, height = $height")

        // 设置渲染的OpenGL场景（视口）的位置和大小
        GLES31.glViewport(0, 0, width, height)

        // 计算透视投影矩阵 (Project Matrix)，而后将应用于onDrawFrame（）方法中的对象坐标
        val ratio: Float = width.toFloat() / height.toFloat()
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    override fun onDrawFrame(unused: GL10) {
        // 首先清理屏幕，重绘背景颜色
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)

        // 设置相机的位置，进而计算出视图矩阵 (View Matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // 处理旋转
        setupRotation()

        // 视图转换：计算模型视图投影矩阵MVPMatrix，该矩阵可以将模型空间的坐标转换为归一化设备空间坐标
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)

        mTriangle.draw(mMVPMatrix)
    }

    // 处理旋转
    private fun setupRotation() {
        val time = SystemClock.uptimeMillis() % 4000L
        val angle = 0.045f * time.toInt()

        // 进行旋转变换
        Matrix.rotateM(mViewMatrix, 0, angle, 0f, 0f, 1.0f)
    }

    /**
     * 在 SurfaceView中通过触摸事件获取到要视图矩阵旋转的角度
     * 由于渲染器代码在与应用程序的主用户界面线程在不同的线程上运行，因此必须将此公共变量声明为volatile。
     */
    @Volatile
    var mAngle = 0f

    fun getAngle(): Float {
        return mAngle
    }

    fun setAngle(angle: Float) {
        mAngle = angle
    }

}