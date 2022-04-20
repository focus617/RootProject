package com.focus617.platform.helper

import android.content.Context
import android.content.res.Resources
import timber.log.Timber
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

    /**
     * Reads in text from a file in disk and returns a String containing the
     * text.
     */
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

    /**
     * Reads in text from a resource file and returns a String containing the
     * text.
     */
    fun loadFromResourceFile(context: Context, resourceId: Int): String {

        Timber.d("loadFromResourceFile($resourceId)")

        val body = StringBuilder()

        try {
            val inputStream = context.resources.openRawResource(resourceId)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var nextLine: String?

            while (bufferedReader.readLine().also { nextLine = it } != null) {
                body.append(nextLine)
                body.append('\n')
            }
        } catch (e: IOException) {
            throw RuntimeException("Could not open resource: $resourceId $ e")
        } catch (nfe: Resources.NotFoundException) {
            throw RuntimeException("Resource not found: $resourceId$nfe")
        }
        return body.toString()
    }

    /**
     * Reads in text from a assets file and returns a String containing the
     * text.
     */
    fun loadFromAssetsFile(context: Context, filePath: String): String {

        Timber.d("loadFromAssetsFile($filePath)")

        val body = StringBuilder()

        try {
            val inputStream = context.resources.assets.open(filePath)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var nextLine: String?

            while (bufferedReader.readLine().also { nextLine = it } != null) {
                body.append(nextLine)
                body.append('\n')
            }
        } catch (e: IOException) {
            throw RuntimeException("Could not open shader file: $filePath $ e")
        }
        return body.toString()
    }
}
