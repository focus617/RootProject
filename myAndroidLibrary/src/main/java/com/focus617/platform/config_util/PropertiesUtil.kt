package com.focus617.platform.config_util

import android.content.Context
import android.text.TextUtils
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.util.*

object PropertiesUtil {
    // 默认在assets文件夹下
    private const val ConfigFileName = "appConfig.properties"

    fun loadProperties(context: Context, filePathName: String = ConfigFileName): Properties? {

        Timber.d("loadProperties(): $filePathName")

        if (filePathName.isEmpty() or TextUtils.isEmpty(filePathName)) {
            Timber.w("File doesn't exist")
        }

        val properties = Properties()
        try {
            val inputStream: InputStream = context.assets.open(filePathName)
            properties.load(inputStream)
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return properties
    }
}
