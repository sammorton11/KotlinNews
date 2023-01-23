package com.samm.practiceapp01

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.samm.practiceapp01.data.database.NewsDao
import com.samm.practiceapp01.data.database.NewsDatabase
import com.samm.practiceapp01.domain.repository.Repository
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

// In-progress
class RepositoryDatabaseTest {
    @get:Rule(order = 0)
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var repository: Repository
    private lateinit var db: NewsDatabase
    private lateinit var dao: NewsDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java).build()
        dao = db.myDao()
        repository = FakeRepository(FakeNewsApi, dao)
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        db.close()
    }

    @Test
    fun databaseTest(): Unit = runBlocking {
        assertThat(
            repository.getNews("", 1).isSuccessful,
            Matchers.equalTo(true)
        )
    }
}