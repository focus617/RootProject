package com.focus617.bookreader.ui.util

import android.content.Intent

object IntentUtil {

    fun createOpenIntent() = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/pdf"
    }
}