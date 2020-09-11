package com.example.pomodoro2.domain.model

import com.example.pomodoro2.platform.domain.BaseAggregateRoot
import java.io.Serializable
import java.util.ArrayList
import java.util.UUID

/**
 * 表示项目（目标）的数据类，用来存储创建的项目，并提供给ProjectFragment
 */
data class Task (
    var id: String = UUID.randomUUID().toString(),
    var title: String,
    var description: String = "",
    private var isCompleted: Boolean = false,
    var imageId: Int,
    var priority: Int,
    var createTime: Long = System.currentTimeMillis(),
    var parentId: String? = null
): BaseAggregateRoot(), Serializable {

/*
    private lateinit var children: MutableList<Task>

    private fun children(): MutableList<Task>{
        if(children == null)
            children = arrayListOf<Task>()
        return children
    }
*/
    val isActive
        get() = !isCompleted

    fun complete(){
        this.isCompleted = true
    }

    fun activate(){
        this.isCompleted = false
    }

    fun addChild(task: Task){
        task.parentId = this.id
        //children().add(task)
    }

    fun removeChild(task: Task){
        task.parentId = null
        //children().remove(task)
    }

    val titleForList: String
        get() = if (title.isNotEmpty()) title else description

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()

    public fun testLogger(){
        LOG.info("${this::class.java.simpleName}: Kotlin-Logger works well.")
    }

    companion object {
        val DefaultTask = Task(
            title = "番茄工作法",
            imageId = 0,
            priority = 1
        )
    }
}

// TODO: build Task Factory in Domain/Repository