package com.example.pomodoro2.data

import com.example.pomodoro2.platform.data.InMemoryDataSource
import com.example.pomodoro2.domain.model.Action
import com.example.pomodoro2.domain.model.Task

class AppInMemoryDataSource :
    InMemoryDataSource {

    private var _selectedTask : Task = Task.DefaultTask
    private var _selectedAction : Action? = null

    override fun setSelectedTask(task: Task) {
        _selectedTask = task
    }

    override fun getSelectedTask() = _selectedTask


    override fun setSelectedActivity(action: Action) {
        _selectedAction = action
    }

    override fun getSelectedActivity(): Action? = _selectedAction

}