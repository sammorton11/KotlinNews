package com.samm.practiceapp01

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.samm.practiceapp01.data.repository.RepositoryImpl
import com.samm.practiceapp01.data.database.NewsDao
import com.samm.practiceapp01.data.database.NewsDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ViewModelTests {

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var repositoryImpl: RepositoryImpl
    private lateinit var db: NewsDatabase
    private lateinit var dao: NewsDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java).build()
        dao = db.myDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun test_empty_database() = runBlocking {
        assertEquals(0, repositoryImpl.getNewsFromDatabase.value?.size)
    }
}
