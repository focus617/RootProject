package com.focus617.app_demo.engine.input

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import com.focus617.core.platform.base.BaseEntity

class SensorInput: BaseEntity(), SensorEventListener {

    // new sensor data received
    override fun onSensorChanged(event: SensorEvent?) {
        LOG.info("onSensorChanged: event=${event}")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        LOG.info("onAccuracyChanged: sensor=${sensor}, accuracy=$accuracy")
    }
}