package com.focus617.platform.uicontroller

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.focus617.platform.event.Event


open class BaseViewModel(application: Application) :
    AndroidViewModel(application) {

    protected val application: BaseApplication = getApplication() as BaseApplication

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = Event(message)
    }

}