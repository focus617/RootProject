package com.example.pomodoro2.platform.data

import com.example.pomodoro2.domain.UserProfile

interface IPreferencesDataSource{

    fun saveUser(user: UserProfile)

    fun loadUser(): UserProfile

}