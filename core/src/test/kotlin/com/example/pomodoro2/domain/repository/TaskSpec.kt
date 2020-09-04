package com.example.pomodoro2.domain.repository

import com.example.pomodoro2.domain.model.Task
import com.example.pomodoro2.platform.domain.BaseSpecification

class ChildTaskSpec(private val parent: Task):BaseSpecification<Task>() {
    override fun isSatisfiedBy(t: Task) = (t.parentId == this.parent.id)
}

class ActiveTaskSpec():BaseSpecification<Task>() {
    override fun isSatisfiedBy(t: Task) = t.isActive
}
