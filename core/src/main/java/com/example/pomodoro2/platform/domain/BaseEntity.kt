package com.example.pomodoro2.platform.domain

import com.example.pomodoro2.platform.logging.ILoggable

abstract class BaseEntity {

    companion object: ILoggable {
        val Log = logger()
    }

}