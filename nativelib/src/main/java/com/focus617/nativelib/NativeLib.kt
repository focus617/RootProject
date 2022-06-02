package com.focus617.nativelib

import android.content.res.AssetManager

class NativeLib {

    /**
     * A native method that is implemented by the 'nativelib' native library,
     * which is packaged with this application.
     */
    companion object {
        // Used to loadFile the 'nativelib' library on application startup.
        init {
            System.loadLibrary("nativelib")
        }

        // Native methods are listed here but implemented in C++ files
        external fun isInitDoneNative(): Boolean

        external fun getGLESVersionNative(): String

        external fun createObjectNative(
            assetManager: AssetManager, pathToInternalDir: String
        )

        external fun deleteObjectNative()

        external fun surfaceCreatedNative()

        external fun surfaceChangedNative(width: Int, height: Int)

        external fun drawFrameNative()

        external fun ndkEmboss(data: IntArray?, width: Int, height: Int)

    }
}