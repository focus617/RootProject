package com.focus617.core.engine.ecs.mine.system

import com.focus617.core.engine.ecs.fleks.AllOf
import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.fleks.IteratingSystem
import com.focus617.core.engine.ecs.mine.component.SceneCamera
import com.focus617.core.engine.ecs.mine.gameobject.Camera
import com.focus617.core.engine.ecs.mine.gameobject.ProjectionType
import com.focus617.core.engine.ecs.mine.static.SceneData
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector3
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.screenTouchEvents.*
import com.focus617.core.platform.event.sensorEvents.SensorRotationEvent
import com.focus617.mylib.helper.DateHelper
import com.focus617.mylib.logging.ILoggable
import com.focus617.mylib.logging.WithLogging

@AllOf([SceneCamera::class])
class SceneCameraSystem : IteratingSystem(), ILoggable {
    private val LOG = logger()

    private var mProjectionType: ProjectionType = ProjectionType.None

    init {
        LOG.info("SceneCameraSystem launched.")
    }

    override fun onTickEntity(entity: Entity) {
        val cameraCmp = world.mapper<SceneCamera>()
        val index = cameraCmp[entity].projectionTypeIndex

        if(mProjectionType.index != index){
            when(index){
                ProjectionType.Perspective.index -> {
                    mProjectionType = ProjectionType.Perspective
                    mCamera.setProjectionType(mProjectionType)
                    mCamera.setPerspective(60f, 0.01f, 1000f)
                    mCamera.setDistance(4f)
                    mCamera.setRotation(Vector3(0f, 0f, 0f))
                    mCamera.setFocusPoint(Point3D(0f, 0.5f, 5.0f))
                }
                else ->{
                    mProjectionType = ProjectionType.Orthographic
                    mCamera.setProjectionType(mProjectionType)
                }
            }

            LOG.info("Switch to $mProjectionType.")
        }

        synchronized(SceneData) {
            SceneData.sProjectionMatrix.setValue(mCamera.mProjectionMatrix)
            SceneData.sViewMatrix.setValue(mCamera.mViewMatrix)
        }
    }

    companion object : WithLogging() {
        private val mCamera = Camera()

        enum class ControllerWorkingMode { Scroll, Zoom }

        private var mode: ControllerWorkingMode = ControllerWorkingMode.Scroll

        private var previousSize: Float = mCamera.mOrthographicSize
        private var previousFov: Float = 1.0f
        private var previousSpan: Float = 1.0f
        private var previousX: Float = 0.0f
        private var previousY: Float = 0.0f

        // Euler angle
        private var pitchX: Float = mCamera.mEulerAngleInDegree.x
        private var yawY: Float = mCamera.mEulerAngleInDegree.y
        private var rollZ: Float = mCamera.mEulerAngleInDegree.z

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
                    previousFov = mCamera.mPerspectiveFOVInDegree
                    previousSpan = event.span
                    mode = ControllerWorkingMode.Zoom
                    event.handleFinished()
                }

                is PinchEvent -> {
                    if (mode == ControllerWorkingMode.Zoom) {
                        // 根据双指间距的变化，计算相对变化量
                        val scaleFactor = previousSpan / event.span
                        when(mCamera.mProjectionType){
                            ProjectionType.Orthographic -> {
                                val size = previousSize * scaleFactor
                                mCamera.setOrthographicSize(size)
                                LOG.info("CameraSystem: on PinchEvent, Size=$size")
                            }

                            ProjectionType.Perspective -> {
                                val fov = previousFov * scaleFactor
                                mCamera.setPerspectiveVerticalFov(fov)
                                LOG.info("CameraSystem: on PinchEvent, fov=$fov")
                            }

                            else -> Unit
                        }

                    }
                    event.handleFinished()
                }

                is PinchEndEvent -> {
                    LOG.info("CameraSystem: on PinchEndEvent")
                    mode = ControllerWorkingMode.Scroll
                    LOG.info("CameraSystem: on PinchEndEvent, ZoomLevel=${mCamera.mOrthographicSize}")
                    event.handleFinished()
                }

                is SensorRotationEvent -> {
//                LOG.info("CameraSystem: on SensorRotationEvent")
                    if(mCamera.mProjectionType == ProjectionType.Perspective) {
                        val factor = 0.01f
                        pitchX = -event.pitchXInDegree * factor
                        yawY = -event.yawYInDegree * factor
                        rollZ = when (event.yawYInDegree.toInt()) {
                            in 0..180 -> event.rollZInDegree * factor
                            else -> -event.rollZInDegree * factor
                        }

                        mCamera.setRotation(Vector3(pitchX, yawY, rollZ))
                        event.handleFinished()
                    }
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