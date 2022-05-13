package com.focus617.nativelib

class NativeLib {

    /**
     * A native method that is implemented by the 'nativelib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String


    companion object {
        // Used to loadFile the 'nativelib' library on application startup.
        init {
            System.loadLibrary("nativelib")
        }
        external fun openGlEsSdkNativeLibraryInit(filePath: String, line: Int)
    }
}