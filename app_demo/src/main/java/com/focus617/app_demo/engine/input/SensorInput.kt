package com.focus617.app_demo.engine.input

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Surface
import com.focus617.app_demo.GameActivity
import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.sensorEvents.SensorRotationEvent

class SensorInput(private val mainActivity: GameActivity) : BaseEntity(), SensorEventListener {
    private val mSensorManager = mainActivity.mSensorManager

    private val mRotationSensor: Sensor =
        mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    private val mAccelerometer =
        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        LOG.info("onAccuracyChanged: sensor=${sensor}, accuracy=$accuracy")
    }

    // new sensor data received
    override fun onSensorChanged(event: SensorEvent?) {
        event?.apply {
//            LOG.info("onSensorChanged: event=(${event.sensor},${event.values},${event.timestamp})")
            when (event.sensor) {
                mRotationSensor -> onOrientation(event)
                else -> {}
            }
        }
    }

    private fun onOrientation(event: SensorEvent) {
        val rotationVector: FloatArray = event.values
        val rotationMatrix = FloatArray(9)
        val adjustedRotationMatrix = FloatArray(9)
        val orientation = FloatArray(3)

        // Relative world axis with respect to the device's axis
        var deviceRelativeAxisX: Int = SensorManager.AXIS_X
        var deviceRelativeAxisY: Int = SensorManager.AXIS_Z

        when (mainActivity.mWindowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> {
                deviceRelativeAxisX = SensorManager.AXIS_X
                deviceRelativeAxisY = SensorManager.AXIS_Z
            }
            Surface.ROTATION_90 -> {
                deviceRelativeAxisX = SensorManager.AXIS_Z
                deviceRelativeAxisY = SensorManager.AXIS_MINUS_X
            }
            Surface.ROTATION_180 -> {
                deviceRelativeAxisX = SensorManager.AXIS_MINUS_X
                deviceRelativeAxisY = SensorManager.AXIS_MINUS_Z
            }
            Surface.ROTATION_270 -> {
                deviceRelativeAxisX = SensorManager.AXIS_MINUS_Z
                deviceRelativeAxisY = SensorManager.AXIS_X
            }
        }

        // Remap the axes of the device screen and adjust the rotation matrix for the device orientation
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector)
        SensorManager.remapCoordinateSystem(
            rotationMatrix,
            deviceRelativeAxisX,
            deviceRelativeAxisY,
            adjustedRotationMatrix
        )

        // Convert the rotation matrix into yaw(azimuth), pitch, roll
        SensorManager.getOrientation(adjustedRotationMatrix, orientation)
        val RadiusToDegree: Float = (-180f / Math.PI).toFloat()
        val yaw: Float = orientation[0] * RadiusToDegree
        val pitch: Float = orientation[1] * RadiusToDegree
        val roll: Float = orientation[2] * RadiusToDegree

        val nativeEvent = SensorRotationEvent(pitch, yaw, roll, event.sensor)
        mainActivity.mGLSurfaceView.mData.callback?.let { it(nativeEvent) }
    }

    fun dumpSensors() {
        val deviceSensors: List<Sensor> = mSensorManager.getSensorList(Sensor.TYPE_ALL)
        LOG.info("MobilePhone's Sensor List")
        deviceSensors.forEach() {
            LOG.info("Type: ${it.name}")
        }
    }
}