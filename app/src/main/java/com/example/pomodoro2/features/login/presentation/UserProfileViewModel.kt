package com.example.pomodoro2.features.login.presentation

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.example.pomodoro2.domain.model.UserProfile
import com.example.pomodoro2.framework.extension.getOrEmpty
import com.example.pomodoro2.features.data.localfile.SharedPreferenceDataSource

/**
 * ViewModel for the login screen.
 */
class UserProfileViewModel(val app: Application) : AndroidViewModel(app) {

    // TODO: modify below to call user Domain interactors(use case)
    private val sharedPreferenceHelper by lazy {
        SharedPreferenceDataSource(app)
    }

    var profileName = ObservableField("")
    var profileEmail = ObservableField("")

    fun saveProfile() {
        var userProfile = UserProfile(
            profileName.getOrEmpty(),
            profileEmail.getOrEmpty()
        )
        sharedPreferenceHelper.saveUser(userProfile)
    }

    fun loadProfile() {
        val profile = sharedPreferenceHelper.loadUser()
        profileName.set(profile.name)
        profileEmail.set(profile.email)
    }

    fun hasFullProfile(): Boolean {
        val profile = sharedPreferenceHelper.loadUser()
        return profile.name.isNotEmpty() && profile.email.isNotEmpty()
    }
}