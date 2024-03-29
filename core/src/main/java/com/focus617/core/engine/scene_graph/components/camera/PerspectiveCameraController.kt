package com.focus617.core.engine.scene_graph.components.camera

import com.focus617.core.engine.math.*
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.scene_graph.core.ParentEntity
import com.focus617.core.engine.scene_graph.core.Transform
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.screenTouchEvents.*
import com.focus617.core.platform.event.sensorEvents.SensorRotationEvent
import com.focus617.mylib.helper.DateHelper

/**
 * Perspective Camera 的 Controller类
 */
class PerspectiveCameraControllerOld(camera: PerspectiveCamera) : CameraController(camera) {
    override lateinit var mParent: ParentEntity
    override var mZoomLevel: Float = 0.05f

    // Viewport size
    override var mWidth: Int = 0
    override var mHeight: Int = 0

    // Euler angle
    private var pitchX: Float = 0f
    private var yawY: Float = 90f

    enum class ControllerWorkingMode { Scroll, Zoom }

    private var mode: ControllerWorkingMode = ControllerWorkingMode.Scroll

    override fun onWindowSizeChange(width: Int, height: Int) {
        mWidth = width
        mHeight = height
        reCalculatePerspectiveProjectionMatrix()
    }

    override fun setPosition(x: Float, y: Float, z: Float) {
        (mCamera as PerspectiveCamera).setPosition(Point3D(x, y, z))
    }

    override fun onRender(shader: Shader, transform: Transform?) {}

    private var previousZoomLevel: Float = 1.0f
    private var previousSpan: Float = 1.0f
    private var previousX: Float = 0.0f
    private var previousY: Float = 0.0f

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
                val sensitivity = 10f
                val deltaX = event.x - previousX
                val deltaY = event.y - previousY

                previousX = event.x
                previousY = event.y

                pitchX += deltaY / sensitivity
                yawY += deltaX / sensitivity
                pitchX = pitchClamp(pitchX)
                yawY = yawClamp(yawY)
                setRotation(pitchX, yawY)

                event.handleFinished()
            }

            is PinchStartEvent -> {
                LOG.info("CameraController: on PinchStartEvent")
                // 记录下本轮缩放操作的基准
                previousZoomLevel = mZoomLevel
                previousSpan = event.span
                mode = ControllerWorkingMode.Zoom
                event.handleFinished()
            }

            is PinchEvent -> {
                if (mode == ControllerWorkingMode.Zoom) {
                    // 根据双指间距的变化，计算相对变化量
                    val scaleFactor = previousSpan / event.span
                    setZoomLevel(previousZoomLevel * scaleFactor)
                    LOG.info("CameraController: on PinchEvent, ZoomLevel=$mZoomLevel")
                }
                event.handleFinished()
            }

            is PinchEndEvent -> {
                LOG.info("CameraController: on PinchEndEvent")
                mode = ControllerWorkingMode.Scroll
                LOG.info("CameraController: on PinchEndEvent, ZoomLevel=$mZoomLevel")
                event.handleFinished()
            }

            is SensorRotationEvent -> {
//                LOG.info("CameraController: on SensorRotationEvent")
                setRotation(-event.pitchXInDegree, -event.yawYInDegree)
                when (event.yawYInDegree.toInt()) {
                    in 0..180 -> setRotation(event.rollZInDegree)
                    else -> setRotation(-event.rollZInDegree)
                }
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
        val matrix = FloatArray(16)

        // 计算透视投影矩阵 (Project Matrix)
        if (mWidth > mHeight) {
            // Landscape
            val aspect: Float = mWidth.toFloat() / mHeight.toFloat()
            val ratio = aspect * mZoomLevel
            XMatrix.frustumM(
                matrix,
                0,
                -ratio,
                ratio,
                -mZoomLevel,
                mZoomLevel,
                0.1f,
                100f
            )
        } else {
            // Portrait or Square
            val aspect: Float = mHeight.toFloat() / mWidth.toFloat()
            val ratio = aspect * mZoomLevel
            XMatrix.frustumM(
                matrix,
                0,
                -mZoomLevel,
                mZoomLevel,
                -ratio,
                ratio,
                0.1f,
                100f
            )
        }
        mProjectionMatrix.setValue(matrix)
    }

    fun getZoomLevel() = mZoomLevel
    fun setZoomLevel(level: Float) {
        mZoomLevel = level
        reCalculatePerspectiveProjectionMatrix()
    }

    fun setRotation(rollZ: Float) {
        (mCamera as PerspectiveCamera).setRotation(rollZ)
    }

    fun setRotation(pitchX: Float = 0f, yawY: Float) {
        (mCamera as PerspectiveCamera).setRotation(pitchX, yawY)
    }

    fun setPosition(position: Point3D) {
        (mCamera as PerspectiveCamera).setPosition(position)
    }

    fun translate(normalizedVector3: Vector3) {
        with((mCamera as PerspectiveCamera)) {
            setPosition(getPosition().translate(normalizedVector3))
        }
    }


}