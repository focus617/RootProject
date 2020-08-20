package com.example.pomodoro2.features.data.database

import android.content.Context
import com.example.pomodoro2.R
import com.example.pomodoro2.platform.data.IDbLikeDataSource
import com.example.pomodoro2.domain.model.Task
import com.example.pomodoro2.features.data.database.TaskConstants.images
import com.example.pomodoro2.features.data.database.TaskConstants.titles
import com.example.pomodoro2.framework.extension.asDatabaseEntity
import com.example.pomodoro2.platform.functional.Result
import com.example.pomodoro2.platform.functional.Result.Success
import com.example.pomodoro2.platform.functional.Result.Error
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

object TaskConstants{
    val titles = arrayOf(
        "点击右下方加号新建任务",
        "点击左侧方框完成任务",
        "点击任务卡片，进入番茄工作时间",
        "左滑屏幕进行设置",
        "查看统计总结过去"
/*
        "读书",
        "锻炼身体",
        "学习Android开发",
        "冥想",
        "工作"
*/
    )

    val images = intArrayOf(
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
class RoomTaskDataSource(val context: Context) :
    IDbLikeDataSource<Task> {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val taskDao = AppDatabase.getInstance(context.applicationContext).taskDao


    override suspend fun createOrUpdateTask(task: Task) {
        withContext(ioDispatcher) {
            taskDao.insertTask(task.asDatabaseEntity())
        }
    }


    override suspend fun retrieveTask(taskId: String): Result<Task> = withContext(ioDispatcher) {
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

    override suspend fun retrieveTasks(): Result<List<Task>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(taskDao.getTasks().asDomainModel())
        } catch (e: Exception) {
            Error(e)
        }
    }

/*    override suspend fun updateTask(task: Task){
        withContext(ioDispatcher) {
            taskDao.updateTask(task.asDatabaseEntity())
        }
    }*/


    override suspend fun deleteTask(task: Task){
        withContext(ioDispatcher) {
            taskDao.deleteTaskById(task.id)
        }
    }

    override suspend fun deleteAllTasks() =  withContext(ioDispatcher) {
        taskDao.deleteTasks()
    }

    override suspend fun completeTask(task: Task) = withContext(ioDispatcher) {
        taskDao.updateCompleted(task.id, true)
    }

    override suspend fun activateTask(task: Task) = withContext(ioDispatcher) {
        taskDao.updateCompleted(task.id, false)
    }

    override suspend fun clearCompletedTasks() = withContext<Unit>(ioDispatcher) {
        taskDao.deleteCompletedTasks()
    }

    /**
     * Create tutorial tasks and insert them into database.
     */
    override suspend fun initializeTutorialTasks() : Result<List<Task>>{

        var tasks = ConcurrentHashMap<String, Task>()

        for ((index, element) in images.withIndex()) {
            // Create a new Task , which captures the current time,
            // then insert it into the database.
            val task = Task(
                title = titles[index],
                imageId = element,
                priority = index + 1
            )
            //taskDao.insertTask(task.asDatabaseEntity())
            tasks.put(task.id, task)
        }
        return Success(tasks.values.sortedBy {it.priority})
    }


}