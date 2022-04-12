package com.focus617.tankwar.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.focus617.mylib.netty.api.IfNorthBoundChannel
import com.focus617.tankwar.MyApplication
import com.focus617.tankwar.ui.game.GameViewModel
import javax.inject.Inject

class MyViewModelFactory @Inject constructor(
    private val application: Application
) : ViewModelProvider.Factory {

    private val northBoundChannel: IfNorthBoundChannel = (application as MyApplication).uiChannel

    override fun <T : ViewModel> create(modelClass: Class<T>): T = with(modelClass) {
        when {
            isAssignableFrom(GameViewModel::class.java) ->
                return modelClass.getConstructor(
                    Application::class.java,
                    IfNorthBoundChannel::class.java
                )
                    .newInstance(application, northBoundChannel)

            else -> throw IllegalStateException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

}
