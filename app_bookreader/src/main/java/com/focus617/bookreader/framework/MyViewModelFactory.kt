package com.focus617.bookreader.framework

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.focus617.bookreader.MyApplication
import com.focus617.bookreader.framework.interactors.Interactors
import com.focus617.platform.uicontroller.BaseViewModel
import javax.inject.Inject

class MyViewModelFactory @Inject constructor(
    private val application: Application
) : ViewModelProvider.Factory {

    private val dependencies: Interactors = (application as MyApplication).interactors

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (BaseViewModel::class.java.isAssignableFrom(modelClass)) {
            return modelClass.getConstructor(Application::class.java, Interactors::class.java)
                .newInstance(application, dependencies)
        } else {
            throw IllegalStateException("ViewModel must extend MyViewModel")
        }
    }

}