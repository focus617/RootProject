package com.example.pomodoro2.features.login.presentation

import androidx.databinding.ObservableField
import com.example.pomodoro2.core.platform.BaseViewModel

/**
 * ViewModel for the login screen.
 */
class UserViewModel(
    //private val getTasksUseCase: GetTasksUseCase
) : BaseViewModel() {

    var profileName = ObservableField("")
    var profileEmail = ObservableField("")
}