package com.focus617.core.engine.ecs.mine.system

import com.focus617.core.engine.ecs.fleks.AllOf
import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.fleks.IteratingSystem
import com.focus617.core.engine.ecs.mine.component.SceneCamera
import com.focus617.core.engine.ecs.mine.objlib.Camera
import com.focus617.core.engine.ecs.mine.objlib.ProjectionType
import com.focus617.core.engine.ecs.mine.static.SceneData
import com.focus617.core.engine.math.Vector3
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.screenTouchEvents.*
import com.focus617.core.platform.event.sensorEvents.SensorRotationEvent
import com.focus617.mylib.helper.DateHelper
import com.focus617.mylib.logging.ILoggable
import com.focus617.mylib.logging.WithLogging

@AllOf([SceneCamera::class])
class PerspectiveCameraSystem : IteratingSystem(), ILoggable {
    private val LOG = logger()

    private val matrixMapper = world.mapper<Camera>()

    init {
        LOG.info("PerspectiveCameraSystem launched.")
    }

    override fun onTickEntity(entity: Entity) {
        synchronized(SceneData) {
            SceneData.sProjectionMatrix.setValue(mCamera.mProjectionMatrix)
            SceneData.sViewMatrix.setValue(mCamera.mViewMatrix)
        }
    }

    companion object : WithLogging() {
        private val mCamera = Camera()

        init {
            mCamera.setProjectionType(ProjectionType.Perspective)
        }

        enum class ControllerWorkingMode { Scroll, Zoom }

        private var mode: ControllerWorkingMode = ControllerWorkingMode.Scroll

        private var previousFov: Float = 1.0f
        private var previousSpan: Float = 1.0f
        private var previousX: Float = 0.0f
        private var previousY: Float = 0.0f

        // Euler angle
        private var pitchX: Float = 0f
        private var yawY: Float = 90f
        private var rollZ: Float = 0f

        fun onEvent(event: Event): Boolean {
            when (event) {
                is TouchPressEvent -> {
                    LOG.info("CameraSystem: on TouchPressEvent")
                    mode = ControllerWorkingMode.Scroll
                    previousX = event.x
                    previousY = event.y
                    event.handleFinished()
                }

                is TouchDragEvent -> {
                    LOG.info("CameraSystem: on TouchDragEvent")
                    val sensitivity = 10f
                    val deltaX = event.x - previousX
                    val deltaY = event.y - previousY

                    previousX = event.x
                    previousY = event.y

//                    pitchX += deltaY / sensitivity
//                    yawY += deltaX / sensitivity
//                    pitchX = pitchClamp(pitchX)
//                    yawY = yawClamp(yawY)
//                    mCamera.setRotation(Vector3(pitchX, yawY, rollZ))

                    event.handleFinished()
                }

                is PinchStartEvent -> {
                    LOG.info("CameraSystem: on PinchStartEvent")
                    // 记录下本轮缩放操作的基准
                    previousFov = mCamera.mPerspectiveFOVInDegree
                    previousSpan = event.span
                    mode = ControllerWorkingMode.Zoom
                    event.handleFinished()
                }

                is PinchEvent -> {
                    if (mode == ControllerWorkingMode.Zoom) {
                        // 根据双指间距的变化，计算相对变化量
                        val scaleFactor = previousSpan / event.span
                        val fov = previousFov * scaleFactor
                        mCamera.setPerspectiveVerticalFov(fov)
                        LOG.info("CameraSystem: on PinchEvent, fov=$fov")
                    }
                    event.handleFinished()
                }

                is PinchEndEvent -> {
                    LOG.info("CameraSystem: on PinchEndEvent")
                    mode = ControllerWorkingMode.Scroll
                    LOG.info("CameraSystem: on PinchEndEvent, fov=${mCamera.mPerspectiveFOVInDegree}")
                    event.handleFinished()
                }

                is SensorRotationEvent -> {
//                LOG.info("CameraSystem: on SensorRotationEvent")

                    pitchX = -event.pitchXInDegree
                    yawY = -event.yawYInDegree
                    rollZ = when (event.yawYInDegree.toInt()) {
                        in 0..180 -> event.rollZInDegree
                        else -> -event.rollZInDegree
                    }
                    mCamera.setRotation(Vector3(pitchX, yawY, rollZ))
                    event.handleFinished()
                }

                else -> {
                    LOG.info("CameraSystem: ${event.name} from ${event.source} received")
                    LOG.info("It's type is ${event.eventType}")
                    LOG.info("It's was submit at ${DateHelper.timeStampAsStr(event.timestamp)}")
                }
            }

            return event.hasBeenHandled
        }

        fun onWindowSizeChange(width: Int, height: Int) {
            mCamera.setViewportSize(width, height)
        }

    }
}