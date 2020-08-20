package com.example.pomodoro2.data

import com.example.pomodoro2.platform.data.InMemoryDataSource
import com.example.pomodoro2.domain.model.Activity
import com.example.pomodoro2.domain.model.Task

class AppInMemoryDataSource :
    InMemoryDataSource {

    private var _selectedTask : Task = Task.DefaultTask
    private var _selectedActivity : Activity? = null

    override fun setSelectedTask(task: Task) {
        _selectedTask = task
    }

    override fun getSelectedTask() = _selectedTask


    override fun setSelectedActivity(activity: Activity) {
        _selectedActivity = activity
    }

    override fun getSelectedActivity(): Activity? = _selectedActivity

}