package com.example.pomodoro2.features.data.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.pomodoro2.R
import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.framework.extension.asDatabaseEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * This is not meant to be a full set of tests.
 * For simplicity, it's just a SMOKE test which is helpful to make sure it works
 * when building the Room before adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var taskDao: TaskDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        taskDao = db.taskDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetProject() {
        val task = Task(
            1L,
            "番茄工作",
            "详细描述",
            false,
            R.drawable.read_book,
            1
        )
        GlobalScope.launch {
            taskDao.insertTask(task.asDatabaseEntity())
            val taskEntity = taskDao.getTaskById(1L)
            assertEquals("番茄工作", taskEntity?.title)
            assertEquals("详细描述", taskEntity?.description)
            assertEquals(false, taskEntity?.isCompleted)
            assertEquals(R.drawable.read_book, taskEntity?.imageId)
            assertEquals(1, taskEntity?.priority)
        }
    }

}