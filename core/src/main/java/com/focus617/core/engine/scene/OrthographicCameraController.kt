package com.focus617.core.engine.scene

import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.screenTouchEvents.*
import com.focus617.mylib.helper.DateHelper

/**
 * Orthographic Camera 的 Controller类
 */
class OrthographicCameraController(private val mCamera: OrthographicCamera) : CameraController() {
    private val mProjectionMatrix = FloatArray(16)
    private var mZoomLevel: Float = 1.0f
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    enum class ControllerWorkingMode { Scroll, Zoom }

    private var mode: ControllerWorkingMode = ControllerWorkingMode.Scroll

    fun getCamera() = mCamera

    fun getZoomLevel() = mZoomLevel
    fun setZoomLevel(level: Float) {
        mZoomLevel = level
        reCalculateOrthoGraphicProjectionMatrix()
        mCamera.setProjectionMatrix(mProjectionMatrix)
    }

    fun setRotation(rollZ: Float = 90f) {
        mCamera.setRotation(rollZ)
    }

    fun setPosition(position: Point3D) {
        mCamera.setPosition(position)
    }

    fun setPosition(x: Float, y: Float, z: Float) {
        mCamera.setPosition(Point3D(x, y, z))
    }

    fun translate(normalizedVector3: Vector3) {
        with(mCamera) {
            setPosition(getPosition().translate(normalizedVector3))
        }
    }

    fun onWindowSizeChange(width: Int, height: Int) {
        mWidth = width
        mHeight = height
        reCalculateOrthoGraphicProjectionMatrix()
        mCamera.setProjectionMatrix(mProjectionMatrix)
    }

    private var mCameraRotation: Float = 0F
    override fun onUpdate(timeStep: TimeStep) {
        val mCameraRotationSpeed: Float = 0.01F

        //mCameraRotation += timeStep.getMilliSecond() * mCameraRotationSpeed
        if (mCameraRotation > 180.0f)
            mCameraRotation -= 360.0f;
        else if (mCameraRotation <= -180.0f)
            mCameraRotation += 360.0f;

        setRotation(mCameraRotation)
    }

    private var previoudZoomLevel: Float = 1.0f
    private var previousSpan: Float = 1.0f
    private var previousX: Float = 0.0f
    private var previousY: Float = 0.0f
    // private var previousZ: Float = 0.0f

    override fun onEvent(event: Event): Boolean {
        when (event) {
            is TouchPressEvent -> {
                LOG.info("CameraController: on TouchPressEvent")
                mode = ControllerWorkingMode.Scroll
                previousX = event.x
                previousY = event.y
                event.handleFinished()
            }

            is TouchDragEvent -> {
                LOG.info("CameraController: on TouchDragEvent")
                val deltaX = previousX - event.x
                val deltaY = event.y - previousY
                // TODO: 需要反向映射到视图空间
                val translation = Vector3(deltaX, deltaY, 0f)
                translate(translation * 0.001f)

                previousX = event.x
                previousY = event.y
                event.handleFinished()
            }

            is PinchStartEvent -> {
                LOG.info("CameraController: on PinchStartEvent")
                // 记录下本轮缩放操作的基准
                previoudZoomLevel = mZoomLevel
                previousSpan = event.span
                mode = ControllerWorkingMode.Zoom
                event.handleFinished()
            }

            is PinchEvent -> {
                if (mode == ControllerWorkingMode.Zoom) {
                    // 根据双指间距的变化，计算相对变化量
                    val scaleFactor = previousSpan / event.span
                    LOG.info("CameraController: on PinchEvent, ZoomLevel=$scaleFactor")
                    setZoomLevel(previoudZoomLevel * scaleFactor)
                }
                event.handleFinished()
            }

            is PinchEndEvent -> {
                LOG.info("CameraController: on PinchEndEvent")
                mode = ControllerWorkingMode.Scroll
                event.handleFinished()
            }

            else -> {
                LOG.info("CameraController: ${event.name} from ${event.source} received")
                LOG.info("It's type is ${event.eventType}")
                LOG.info("It's was submit at ${DateHelper.timeStampAsStr(event.timestamp)}")
            }
        }

        return event.hasBeenHandled
    }

    private fun reCalculatePerspectiveProjectionMatrix() {
        // 计算透视投影矩阵 (Project Matrix)
        val ratio: Float = mWidth.toFloat() / mHeight.toFloat()
        XMatrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    private fun reCalculateOrthoGraphicProjectionMatrix() {
        // 计算正交投影矩阵 (Project Matrix)
        if (mWidth > mHeight) {
            // Landscape
            val aspect: Float = mWidth.toFloat() / mHeight.toFloat()
            val ratio = aspect * mZoomLevel
            XMatrix.orthoM(
                mProjectionMatrix,
                0,
                -ratio,
                ratio,
                -mZoomLevel,
                mZoomLevel,
                -1.0f,
                1.0f
            )
        } else {
            // Portrait or Square
            val aspect: Float = mHeight.toFloat() / mWidth.toFloat()
            val ratio = aspect * mZoomLevel
            XMatrix.orthoM(
                mProjectionMatrix,
                0,
                -mZoomLevel,
                mZoomLevel,
                -ratio,
                ratio,
                -1.0f,
                1.0f
            )
        }
    }

}