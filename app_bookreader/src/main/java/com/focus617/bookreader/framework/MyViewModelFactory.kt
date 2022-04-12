package com.focus617.bookreader.framework

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.focus617.bookreader.MyApplication
import com.focus617.bookreader.framework.interactors.Interactors
import com.focus617.bookreader.ui.home.HomeViewModel
import com.focus617.bookreader.ui.slideshow.SlideshowViewModel
import com.focus617.mylib.netty.api.IfNorthBoundChannel
import javax.inject.Inject

class MyViewModelFactory @Inject constructor(
    private val application: Application
) : ViewModelProvider.Factory {

    private val dependencies: Interactors = (application as MyApplication).interactors
    private val northBoundChannel: IfNorthBoundChannel = (application as MyApplication).uiChannel

    override fun <T : ViewModel> create(modelClass: Class<T>): T = with(modelClass) {
        when {
            isAssignableFrom(HomeViewModel::class.java) ->
                return modelClass.getConstructor(Application::class.java, Interactors::class.java)
                    .newInstance(application, dependencies)

            isAssignableFrom(SlideshowViewModel::class.java) ->
                return modelClass.getConstructor(
                    Application::class.java,
                    IfNorthBoundChannel::class.java
                )
                    .newInstance(application, northBoundChannel)

            else -> throw IllegalStateException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

}
