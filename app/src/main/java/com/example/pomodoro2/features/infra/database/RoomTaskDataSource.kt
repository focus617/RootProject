package com.example.pomodoro2.features.infra.database

import android.content.Context
import com.example.pomodoro2.R
import com.example.pomodoro2.data.TaskDataSource
import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.features.infra.database.TaskConstants.images
import com.example.pomodoro2.features.infra.database.TaskConstants.titles
import com.example.pomodoro2.framework.extension.asDatabaseEntity

object TaskConstants{
    val titles = arrayOf(
        "点击右下方加号新建任务",
        "点击左侧方框完成任务",
        "点击任务卡片，进入番茄工作时间",
        "左滑屏幕进行设置",
        "查看统计总结过去",
        "读书",
        "锻炼身体",
        "学习Android开发",
        "冥想",
        "工作"
    )

    val images = intArrayOf(
        R.drawable.read_book,
        R.drawable.exercise,
        R.drawable.study,
        R.drawable.thinking,
        R.drawable.work,
        R.drawable.read_book,
        R.drawable.exercise,
        R.drawable.study,
        R.drawable.thinking,
        R.drawable.work
    )
}

class RoomTaskDataSource(val context: Context) : TaskDataSource {

    private val taskDao = AppDatabase.getInstance(context.applicationContext).taskDao

    override suspend fun add(task: Task){
        taskDao.insertTask(task.asDatabaseEntity())
    }

    override suspend fun getAll(): List<Task> = taskDao.getTasks().asDomainModel()

    override suspend fun remove(task: Task){
        taskDao.removeTask(task.asDatabaseEntity())
    }

    override suspend fun removeAll() {
        taskDao.clearTaskTable()
    }

    /**
     * Create tutorial tasks and insert them into database.
     */
    override suspend fun initializeTutorialTasks() {

        for ((index, element) in images.withIndex()) {
            // Create a new Task , which captures the current time,
            // then insert it into the database.
            val task = TaskEntity(
                title = titles[index],
                imageId = element,
                priority = index + 1
            )
            taskDao.insertTask(task)
        }
    }


}