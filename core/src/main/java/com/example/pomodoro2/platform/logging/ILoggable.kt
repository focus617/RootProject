package com.example.pomodoro2.platform.logging

import mu.KotlinLogging.logger
import org.slf4j.Logger

interface ILoggable {

    fun ILoggable.logger(): Logger = logger(this.javaClass.name)

}