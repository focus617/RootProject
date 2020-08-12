package com.example.pomodoro2.features.infra.database

import com.example.pomodoro2.data.InMemoryTaskDataSource
import com.example.pomodoro2.domain.Task

class InMemoryTaskDataSource : InMemoryTaskDataSource {

    private var _selectedTask : Task = Task.FirstTask

    override fun setSelectedTask(task: Task) {
        _selectedTask = task
    }

    override fun getSelectedTask() = _selectedTask

}