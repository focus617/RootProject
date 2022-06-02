package com.focus617.modeldemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import com.focus617.nativelib.NativeLib
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GraphicsRenderer : GLSurfaceView.Renderer {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        NativeLib.surfaceCreatedNative()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        NativeLib.surfaceChangedNative(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        NativeLib.drawFrameNative()
    }

    fun loadBitmap(context: Context) {
        // Load the bitmap into an array
        val b: Bitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.bird
        )
        var pix: IntArray = IntArray(b.width * b.height)
        b.getPixels(pix, 0, b.width, 0, 0, b.width, b.height)

        // Call the native method 'ndkEmboss' to update pixel array
        NativeLib.ndkEmboss(pix, b.width, b.height)

        // Create a new bitmap with the result array
        val out: Bitmap = Bitmap.createBitmap(
            b.width, b.height, Bitmap.Config.ARGB_8888
        )
        out.setPixels(pix, 0, b.width, 0, 0, b.width, b.height)

        // Clean up
        pix = IntArray(0)
        b.recycle()
    }

}
