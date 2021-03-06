package com.example.pomodoro2.features.login.presentation

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.example.pomodoro2.framework.extension.getOrEmpty
import com.example.pomodoro2.framework.helper.SharedPreferenceHelper

/**
 * ViewModel for the login screen.
 */
class UserProfileViewModel(val app: Application) : AndroidViewModel(app) {

    private val sharedPreferenceHelper by lazy { SharedPreferenceHelper(app) }

    var profileName = ObservableField("")
    var profileEmail = ObservableField("")

    fun saveProfile() {
        sharedPreferenceHelper.saveProfile(profileName.getOrEmpty(), profileEmail.getOrEmpty())
    }

    fun loadProfile() {
        val profile = sharedPreferenceHelper.getProfile()
        profileName.set(profile.name)
        profileEmail.set(profile.email)
    }

    fun hasFullProfile(): Boolean {
        val profile = sharedPreferenceHelper.getProfile()
        return profile.name.isNotEmpty() && profile.email.isNotEmpty()
    }
}