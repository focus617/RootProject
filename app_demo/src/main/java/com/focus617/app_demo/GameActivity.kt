package com.focus617.app_demo

import android.app.ActivityManager
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.focus617.app_demo.engine.AndroidWindow
import com.focus617.app_demo.engine.Sandbox
import com.focus617.app_demo.engine.XGLRenderer

class GameActivity : AppCompatActivity() {

    // EntryPoint for XGame
    lateinit var game: Sandbox

    private lateinit var mGLSurfaceView: GLSurfaceView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 创建一个GLSurfaceView实例,并将其设置为此Activity的ContentView。
        mGLSurfaceView = AndroidWindow.createWindow(this)
//        if (isES3Supported()) {
//            // Request an OpenGL ES 3.0 compatible context.
//            mGLSurfaceView.setEGLContextClientVersion(3)
//        }
//        else{

            // Request an OpenGL ES 2.0 compatible context.
            mGLSurfaceView.setEGLContextClientVersion(2)
//        }

        // 设置渲染器（Renderer）以在GLSurfaceView上绘制
        mGLSurfaceView.setRenderer(XGLRenderer())

        setContentView(mGLSurfaceView)

        // 以这个GLSurfaceView实例作为Sandbox的Window，初始化引擎
        game = Sandbox(mGLSurfaceView as AndroidWindow)
    }

    override fun onResume() {
        super.onResume()
        mGLSurfaceView.onResume()
    }


    override fun onPause() {
        super.onPause()
        mGLSurfaceView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        game.onDestroy()
    }

    // Check if the system supports OpenGL ES 3.0.
    private fun isES3Supported(): Boolean{
        var supportsEs3 = false
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo

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
        supportsEs3 = (configurationInfo.reqGlEsVersion >= 0x30000
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86"))))

        if(!supportsEs3) {
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
            Toast.makeText(
                this, "This device does not support OpenGL ES 3.0.",
                Toast.LENGTH_LONG
            ).show()
        }
        return supportsEs3
    }

}