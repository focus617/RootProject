package com.focus617.core.ecs.mine.system

import com.focus617.core.ecs.fleks.AllOf
import com.focus617.core.ecs.fleks.Entity
import com.focus617.core.ecs.fleks.IteratingSystem
import com.focus617.core.ecs.mine.component.Camera
import com.focus617.core.ecs.mine.component.CameraMatrix
import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.engine.math.pitchClamp
import com.focus617.core.engine.math.yawClamp
import com.focus617.core.engine.scene_graph.components.camera.PerspectiveCamera
import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.screenTouchEvents.*
import com.focus617.core.platform.event.sensorEvents.SensorRotationEvent
import com.focus617.mylib.helper.DateHelper
import com.focus617.mylib.logging.ILoggable

@AllOf([Camera::class, CameraMatrix::class])
class PerspectiveCameraSystem : IteratingSystem(), ILoggable {
    private val LOG = logger()

    private val matrixMapper = world.mapper<CameraMatrix>()

    override fun onTickEntity(entity: Entity) {
        LOG.info("entity id = ${entity.id} ")

        if(isDirty){
            reCalculatePerspectiveProjectionMatrix()
            isDirty = false
        }

        val matrix = matrixMapper[entity]
        matrix.projectionMatrix.setValue(mProjectionMatrix)
        matrix.viewMatrix.setValue(mCamera.getViewMatrix())
    }

    companion object {
        private val mCamera = PerspectiveCamera()
        private val mProjectionMatrix = Mat4()
//        private val mViewMatrix = Mat4()
        private var isDirty: Boolean = true
        fun setDirty() {
            isDirty = true
        }

        private var mZoomLevel: Float = 0.05f

        // Viewport size
        private var mWidth: Int = 0
        private var mHeight: Int = 0

        // Euler angle
        private var pitchX: Float = 0f
        private var yawY: Float = 90f

        enum class ControllerWorkingMode { Scroll, Zoom }

        private var mode: ControllerWorkingMode = ControllerWorkingMode.Scroll

        private var previousZoomLevel: Float = 1.0f
        private var previousSpan: Float = 1.0f
        private var previousX: Float = 0.0f
        private var previousY: Float = 0.0f

        fun onEvent(event: Event): Boolean {
            when (event) {
                is TouchPressEvent -> {
                    BaseEntity.LOG.info("CameraController: on TouchPressEvent")
                    mode = ControllerWorkingMode.Scroll
                    previousX = event.x
                    previousY = event.y
                    event.handleFinished()
                }

                is TouchDragEvent -> {
                    BaseEntity.LOG.info("CameraController: on TouchDragEvent")
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
                    BaseEntity.LOG.info("CameraController: on PinchStartEvent")
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
                        BaseEntity.LOG.info("CameraController: on PinchEvent, ZoomLevel=$mZoomLevel")
                    }
                    event.handleFinished()
                }

                is PinchEndEvent -> {
                    BaseEntity.LOG.info("CameraController: on PinchEndEvent")
                    mode = ControllerWorkingMode.Scroll
                    BaseEntity.LOG.info("CameraController: on PinchEndEvent, ZoomLevel=$mZoomLevel")
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
                    BaseEntity.LOG.info("CameraController: ${event.name} from ${event.source} received")
                    BaseEntity.LOG.info("It's type is ${event.eventType}")
                    BaseEntity.LOG.info("It's was submit at ${DateHelper.timeStampAsStr(event.timestamp)}")
                }
            }

            return event.hasBeenHandled
        }

        fun onWindowSizeChange(width: Int, height: Int) {
            mWidth = width
            mHeight = height
            reCalculatePerspectiveProjectionMatrix()
        }

        private fun setRotation(pitchX: Float = 0f, yawY: Float) {
            mCamera.setRotation(pitchX, yawY)
        }

        private fun setRotation(rollZ: Float) {
            mCamera.setRotation(rollZ)
        }

        private fun setZoomLevel(level: Float) {
            mZoomLevel = level
            reCalculatePerspectiveProjectionMatrix()
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
    }
}