package com.example.pomodoro2.features.infra.database

import com.example.pomodoro2.data.SelectedTaskDataSource
import com.example.pomodoro2.domain.Task

class InMemorySelectedTaskDataSource : SelectedTaskDataSource {
    private var _selectedTask : Task = Task.FirstTask

    override fun setSelectedTask(task: Task) {
        _selectedTask = task
    }

    override fun getSelectedTask() = _selectedTask

}