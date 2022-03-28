package com.focus617.platform.helper

import android.content.Context
import java.io.*

object FileHelper {

    private const val DATA_FILE_NAME = "data"

    fun save(context: Context, inputText: String, fileName: String = DATA_FILE_NAME) {
        try {
            val output = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            val writer = BufferedWriter(OutputStreamWriter(output))
            writer.use {
                it.write(inputText)
                it.write("\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun load(context: Context, fileName: String = DATA_FILE_NAME): String {
        val content = StringBuilder()
        try {
            val input = context.openFileInput(fileName)
            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    content.append(it)
                    content.append("\n")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return content.toString()
    }

}