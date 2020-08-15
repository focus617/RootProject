package com.example.pomodoro2.features.data.memory

import com.example.pomodoro2.data.interface_def.InMemoryDataSource
import com.example.pomodoro2.domain.Activity
import com.example.pomodoro2.domain.Task

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