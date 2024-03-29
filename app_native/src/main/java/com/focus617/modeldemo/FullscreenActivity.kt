package com.focus617.modeldemo

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.focus617.modeldemo.databinding.ActivityFullscreenBinding
import com.focus617.nativelib.NativeLib
import com.focus617.opengles.egl.ConfigChooser
import com.focus617.opengles.egl.ContextFactory
import timber.log.Timber

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenBinding
    private lateinit var glSurfaceView: GLSurfaceView
    private val renderer = GraphicsRenderer()

    private lateinit var gestureObject: GestureClass

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // loadFile Native Library
        Timber.i("On Create Method Calling Native Library")
        val nativeLib = NativeLib()
        val assetManager: AssetManager = assets
        val pathToInternalDir = filesDir.absolutePath

        NativeLib.createObjectNative(assetManager, pathToInternalDir)

        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isFullscreen = true

        // Set up the user interaction to manually show or hide the system UI.
        glSurfaceView = binding.glSurfaceview
        // Request an OpenGL ES 3.0 compatible context.
        glSurfaceView.setEGLContextClientVersion(3)
        glSurfaceView.setEGLContextFactory(ContextFactory())
        glSurfaceView.setEGLConfigChooser(ConfigChooser())
        glSurfaceView.setRenderer(renderer)
        glSurfaceView.setOnClickListener { toggle() }

        fullscreenContentControls = binding.fullscreenContentControls

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
//        binding.dummyButton.setOnTouchListener(delayHideTouchListener)

        // mGestureObject will handle touch gestures on the screen
        gestureObject = GestureClass(this);
        glSurfaceView.setOnTouchListener(gestureObject.TwoFingerGestureListener);

    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Delete native objects
        NativeLib.deleteObjectNative()
    }


    private lateinit var fullscreenContentControls: LinearLayout
    private var isFullscreen: Boolean = false
    private val hideHandler = Handler(Looper.myLooper()!!)
    private val hideRunnable = Runnable { hide() }

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar
        if (Build.VERSION.SDK_INT >= 30) {
            glSurfaceView.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            glSurfaceView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
        fullscreenContentControls.visibility = View.VISIBLE
    }



    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
//    private val delayHideTouchListener = View.OnTouchListener { view, motionEvent ->
//        when (motionEvent.action) {
//            MotionEvent.ACTION_DOWN -> if (AUTO_HIDE) {
//                delayedHide(AUTO_HIDE_DELAY_MILLIS)
//            }
//            MotionEvent.ACTION_UP -> view.performClick()
//            else -> {
//            }
//        }
//        false
//    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    fun delayedHide(delayMillis: Int = AUTO_HIDE_DELAY_MILLIS) {
        if (AUTO_HIDE) {
            hideHandler.removeCallbacks(hideRunnable)
            hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
        }
    }

    private fun toggle() {
        if (isFullscreen) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        fullscreenContentControls.visibility = View.GONE
        isFullscreen = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            glSurfaceView.windowInsetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            glSurfaceView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        isFullscreen = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }


    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }
}