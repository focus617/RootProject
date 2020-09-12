package com.example.backend.utils

import com.example.pomodoro2.domain.model.Task
import com.example.pomodoro2.platform.domain.BaseSpecification

class ofIdTaskSpec(private val id: String):BaseSpecification<Task>() {
    override fun isSatisfiedBy(t: Task) = (t.id == id)
}

class ActiveTaskSpec():BaseSpecification<Task>() {
    override fun isSatisfiedBy(t: Task) = t.isActive
}
