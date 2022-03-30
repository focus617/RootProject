package com.focus617.tankwar.ui.game

import android.app.Application
import com.focus617.platform.uicontroller.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class GameViewModel(application: Application) : BaseViewModel(application) {

    // Declare Job() and cancel jobs in onCleared().
    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // Define uiScope for coroutines.
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

}