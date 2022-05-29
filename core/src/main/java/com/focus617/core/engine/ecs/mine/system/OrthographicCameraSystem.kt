package com.focus617.core.engine.ecs.mine.system

import com.focus617.core.engine.ecs.fleks.AllOf
import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.fleks.IteratingSystem
import com.focus617.core.engine.ecs.mine.component.CameraMatrix
import com.focus617.core.engine.ecs.mine.component.OrthographicCameraCmp
import com.focus617.core.engine.ecs.mine.objlib.Camera
import com.focus617.core.engine.ecs.mine.objlib.ProjectionType
import com.focus617.core.engine.ecs.mine.static.SceneData
import com.focus617.core.engine.math.Point3D
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.screenTouchEvents.*
import com.focus617.mylib.helper.DateHelper
import com.focus617.mylib.logging.ILoggable
import com.focus617.mylib.logging.WithLogging

@AllOf([CameraMatrix::class, OrthographicCameraCmp::class])
class OrthographicCameraSystem : IteratingSystem(), ILoggable {
    private val LOG = logger()

    private val matrixMapper = world.mapper<CameraMatrix>()

    init {
        LOG.info("OrthographicCameraSystem launched.")
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
            mCamera.setProjectionType(ProjectionType.Orthographic)
        }

        enum class ControllerWorkingMode { Scroll, Zoom }

        private var mode: ControllerWorkingMode = ControllerWorkingMode.Scroll

        private var previousSize: Float = mCamera.mOrthographicSize
        private var previousSpan: Float = 1.0f
        private var previousX: Float = 0.0f
        private var previousY: Float = 0.0f

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
                    val deltaX = event.x - previousX
                    val deltaY = event.y - previousY

                    previousX = event.x
                    previousY = event.y

                    val factor = 0.002f
                    val x = mCamera.mFocusPoint.x - deltaX * factor
                    val y = mCamera.mFocusPoint.y + deltaY * factor
                    val z = mCamera.mFocusPoint.z
                    mCamera.setFocusPoint(Point3D(x, y, z))

                    event.handleFinished()
                }

                is PinchStartEvent -> {
                    LOG.info("CameraSystem: on PinchStartEvent")
                    // 记录下本轮缩放操作的基准
                    previousSize = mCamera.mOrthographicSize
                    previousSpan = event.span
                    mode = ControllerWorkingMode.Zoom
                    event.handleFinished()
                }

                is PinchEvent -> {
                    if (mode == ControllerWorkingMode.Zoom) {
                        // 根据双指间距的变化，计算相对变化量
                        val scaleFactor = previousSpan / event.span
                        val size = previousSize * scaleFactor
                        mCamera.setOrthographicSize(size)
                        LOG.info("CameraSystem: on PinchEvent, Size=$size")
                    }
                    event.handleFinished()
                }

                is PinchEndEvent -> {
                    LOG.info("CameraSystem: on PinchEndEvent")
                    mode = ControllerWorkingMode.Scroll
                    LOG.info("CameraSystem: on PinchEndEvent, ZoomLevel=${mCamera.mOrthographicSize}")
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