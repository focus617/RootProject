package com.focus617.core.engine.ecs.mine.system

import com.focus617.core.engine.ecs.fleks.AllOf
import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.fleks.IteratingSystem
import com.focus617.core.engine.ecs.mine.component.CameraMatrix
import com.focus617.core.engine.ecs.mine.component.OrthographicCameraCmp
import com.focus617.core.engine.ecs.mine.static.SceneData
import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.engine.scene_graph.components.camera.OrthographicCamera
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
        if (isDirty) {
            LOG.info("OrthographicCameraSystem onTickEntity.")

            reCalculateOrthoGraphicProjectionMatrix()
            synchronized(SceneData) {
                SceneData.sProjectionMatrix.setValue(mProjectionMatrix)
            }
            isDirty = false
        }

        if(mCamera.isDirty()){
            LOG.info("OrthographicCameraSystem onTickEntity.")

            synchronized(SceneData) {
                SceneData.sViewMatrix.setValue(mCamera.getViewMatrix())
            }
        }
    }

    companion object : WithLogging() {
        private val mCamera = OrthographicCamera()
        private val mProjectionMatrix = Mat4()

        private var isDirty: Boolean = true
        fun setDirty() {
            isDirty = true
        }

        private var mZoomLevel: Float = 0.85f

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

                    val translation = Vector3(deltaX, -deltaY, 0f)
                    translate(translation * 0.001f)

                    event.handleFinished()
                }

                is PinchStartEvent -> {
                    LOG.info("CameraSystem: on PinchStartEvent")
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
                        LOG.info("CameraSystem: on PinchEvent, ZoomLevel=$mZoomLevel")
                    }
                    event.handleFinished()
                }

                is PinchEndEvent -> {
                    LOG.info("CameraSystem: on PinchEndEvent")
                    mode = ControllerWorkingMode.Scroll
                    LOG.info("CameraSystem: on PinchEndEvent, ZoomLevel=$mZoomLevel")
                    event.handleFinished()
                }

//                is SensorRotationEvent -> {
//                LOG.info("CameraSystem: on SensorRotationEvent")
//                    setRotation(-event.pitchXInDegree, -event.yawYInDegree)
//                    when (event.yawYInDegree.toInt()) {
//                        in 0..180 -> setRotation(event.rollZInDegree)
//                        else -> setRotation(-event.rollZInDegree)
//                    }
//                    event.handleFinished()
//                }

                else -> {
                    LOG.info("CameraSystem: ${event.name} from ${event.source} received")
                    LOG.info("It's type is ${event.eventType}")
                    LOG.info("It's was submit at ${DateHelper.timeStampAsStr(event.timestamp)}")
                }
            }

            return event.hasBeenHandled
        }

        fun onWindowSizeChange(width: Int, height: Int) {
            mWidth = width
            mHeight = height
            setDirty()
        }

        private fun translate(normalizedVector3: Vector3) {
            with(mCamera) {
                setPosition(getPosition().translate(normalizedVector3))
            }
        }

        private fun setZoomLevel(level: Float) {
            mZoomLevel = level
            setDirty()
        }

        private fun reCalculateOrthoGraphicProjectionMatrix() {
            val matrix = FloatArray(16)

            // 计算正交投影矩阵 (Project Matrix)
            // 默认绘制的区间在横轴[-1.7778f, 1.778f]，纵轴[-1, 1]之间
            if (mWidth > mHeight) {
                // Landscape
                val aspect: Float = mWidth.toFloat() / mHeight.toFloat()
                val ratio = aspect * mZoomLevel
                // 用ZoomLevel来表示top，因为拉近镜头时，ZoomLevel变大，而对应可见区域会变小
                XMatrix.orthoM(
                    matrix,
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
                    matrix,
                    0,
                    -mZoomLevel,
                    mZoomLevel,
                    -ratio,
                    ratio,
                    -1.0f,
                    1.0f
                )
            }
            mProjectionMatrix.setValue(matrix)
        }
    }
}