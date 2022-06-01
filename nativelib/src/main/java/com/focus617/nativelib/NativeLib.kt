package com.focus617.nativelib

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
        external fun init(width: Int, height: Int)
        external fun step()

        external fun ndkEmboss(data: IntArray?, width: Int, height: Int)
    }
}