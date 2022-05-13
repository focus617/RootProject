package com.focus617.app_demo

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.focus617.app_demo.engine.AndroidWindow
import com.focus617.app_demo.engine.input.SensorInput
import com.focus617.nativelib.NativeLib
import com.focus617.platform.uicontroller.BaseActivity
import timber.log.Timber

class GameActivity : BaseActivity() {

    lateinit var mGLSurfaceView: AndroidWindow

    // Used for sensor detection
    private lateinit var sensorEventListener: SensorInput
    lateinit var mSensorManager: SensorManager

    lateinit var mWindowManager: WindowManager  // 有什么用？

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.i("On Create Method Calling Native Library")
        NativeLib.init()

        mWindowManager = windowManager
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorEventListener = SensorInput(this)

//        sensorEventListener.dumpSensors()

        // 创建一个GLSurfaceView实例,并将其设置为此Activity的ContentView。
        mGLSurfaceView = AndroidWindow.createWindow(
            this,
            (application as MyApplication).gameEngine
        )
        setContentView(mGLSurfaceView)
        setupFullScreen()
        // 设置为竖屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    override fun onResume() {
        super.onResume()
        mGLSurfaceView.onResume()

        // 以这个GLSurfaceView实例作为Sandbox的Window, start render
        (application as MyApplication).gameEngine.onAttachWindow(mGLSurfaceView)

        // 注册各种传感器的事件监听器
        mSensorManager.apply {
            val mRotationSensor = getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            registerListener(
                sensorEventListener,
                mRotationSensor,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }


    override fun onPause() {
        super.onPause()

        // Stop render window UI
        (application as MyApplication).gameEngine.onDetachWindow()

        mGLSurfaceView.onPause()
        // Stop receiving updates from the sensor
        mSensorManager.unregisterListener(sensorEventListener)
    }

    override fun onDestroy() {
        mGLSurfaceView.close()
        super.onDestroy()
    }

    // Check if the system supports OpenGL ES 3.0.
    fun isES3Supported(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo

        val supportsEs3 = if ((application as MyApplication).isEmulator()) {
            Timber.i("Program is running on Emulator")
            toast("Program is running on Emulator.")
            false
        }
        // Even though the latest emulator supports OpenGL ES 3.0,
        // it has a bug where it doesn't set the reqGlEsVersion so
        // the above check doesn't work. The below will detect if the
        // app is running on an emulator, and assume that it supports
        // OpenGL ES 3.0.
        // Even though the latest emulator supports OpenGL ES 3.0,
        // it has a bug where it doesn't set the reqGlEsVersion so
        // the above check doesn't work. The below will detect if the
        // app is running on an emulator, and assume that it supports
        // OpenGL ES 3.0.
        else {
            (configurationInfo.reqGlEsVersion >= 0x30000
                    || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                    && (Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86"))))
        }

        if (!supportsEs3) {
            /*
                 * This is where you could create an OpenGL ES 1.x compatible
                 * renderer if you wanted to support both ES 1 and ES 2/3. Since
                 * we're not doing anything, the app will crash if the device
                 * doesn't support OpenGL ES 3.0. If we publish on the market, we
                 * should also add the following to AndroidManifest.xml:
                 *
                 * <uses-feature android:glEsVersion="0x00030000"
                 * android:required="true" />
                 *
                 * This hides our app from those devices which don't support OpenGL
                 * ES 3.0.
                 */
            toast("This device can't support OpenGL ES 3.0.")
        }
        return supportsEs3
    }
}