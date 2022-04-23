package com.focus617.core.engine.core

import com.focus617.core.platform.base.BaseEntity

class TimeStep(time: Long = 0) : BaseEntity() {
    private var mTime: Long = time

    fun getSeconds(): Long = mTime / 1000
    fun getMilliSecond(): Long = mTime
}