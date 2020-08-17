package com.example.pomodoro2.platform.data

import dagger.Component

//@Component
interface NetworkDataSource{

    fun getTasksNum(): Int

}