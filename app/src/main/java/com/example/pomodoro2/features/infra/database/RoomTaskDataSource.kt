package com.example.pomodoro2.features.infra.database

import android.content.Context
import com.example.pomodoro2.R
import com.example.pomodoro2.data.TaskDataSource
import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.features.infra.database.TaskConstants.images
import com.example.pomodoro2.features.infra.database.TaskConstants.titles
import com.example.pomodoro2.framework.extension.asDatabaseEntity
import com.example.pomodoro2.platform.functional.Result
import com.example.pomodoro2.platform.functional.Result.Success
import com.example.pomodoro2.platform.functional.Result.Error
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TaskConstants{
    val titles = arrayOf(
        "1. 点击右下方加号新建任务",
        "2. 点击左侧方框完成任务",
        "3. 点击任务卡片，进入番茄工作时间",
        "4. 左滑屏幕进行设置",
        "5. 查看统计总结过去",
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

/**
 * Concrete implementation of a data source as a db.
 */
class RoomTaskDataSource(val context: Context) : TaskDataSource {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val taskDao = AppDatabase.getInstance(context.applicationContext).taskDao


    override suspend fun getTask(taskId: Long): Result<Task> = withContext(ioDispatcher) {
        try {
                val task = taskDao.getTaskById(taskId)?.asDomainModel()
                if (task != null) {
                    return@withContext Success(task)
                } else {
                    return@withContext Error(Exception("Task not found!"))
                }
            } catch (e: Exception) {
            return@withContext Error(e)
        }
    }

    override suspend fun getTasks(): Result<List<Task>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(taskDao.getTasks().asDomainModel())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun saveTask(task: Task){
        withContext(ioDispatcher) {
            taskDao.insertTask(task.asDatabaseEntity())
        }
    }


    override suspend fun deleteTask(task: Task){
        withContext(ioDispatcher) {
            taskDao.deleteTaskById(task.id)
        }
    }

    override suspend fun deleteAllTasks() {
        withContext(ioDispatcher) {
            taskDao.deleteTasks()
        }
    }

    override suspend fun completeTask(taskId: Long) {
        withContext(ioDispatcher) {
            taskDao.updateCompleted(taskId, true)
        }
    }

    override suspend fun clearCompletedTasks() = withContext<Unit>(ioDispatcher) {
        taskDao.deleteCompletedTasks()
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