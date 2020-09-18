package com.example.pomodoro2.domain.model

import com.example.pomodoro2.platform.domain.BaseEntity

/**
 * Data Class represents activity to finish task.
 * 表示活动的数据类，代表任务执行的各项活动，并提供给ActivitiesFragment
 */
data class Action(
    var id: Long = 0L,
    /* 如果希望与成员变量的名称不同，请通过name指定列的名称。*/
    var title: String,
    var focusDuration: Long,
    var taskId: String,
    /* 显示的顺序 */
    var priority: Int,
    var createTime: Long = System.currentTimeMillis(),
    var focusCounter: Long = 0L,
    var totalTime: Long = 0L
): BaseEntity()