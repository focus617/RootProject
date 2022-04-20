package com.focus617.platform.uicontroller

import android.app.Application
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager

open class BaseApplication: Application() {

    fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.lowercase().contains("vbox")
                || Build.FINGERPRINT.lowercase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT
                || (this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
            .networkOperatorName.lowercase() == "android")
    }

}