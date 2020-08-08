package com.example.pomodoro2.features.infra.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.pomodoro2.R
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

    private lateinit var projectDAO: ProjectDAO
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
        projectDAO = db.projectDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetProject() {
        val project = Project(1,"番茄工作", R.drawable.read_book,1)
        projectDAO.insert(project)
        val proj = projectDAO.getProjectById(1L)
        assertEquals(proj?.title, "番茄工作")
        assertEquals(proj?.imageId, R.drawable.read_book)
        assertEquals(proj?.priority, 1)
    }

}