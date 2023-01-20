package com.samm.practiceapp01

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.samm.practiceapp01.data.RepositoryImpl
import com.samm.practiceapp01.data.database.NewsDao
import com.samm.practiceapp01.data.database.NewsDatabase
import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.domain.models.NewsItem
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class RepositoryDatabaseTest {

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var repository: RepositoryImpl
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
    fun databaseTest(): Unit = runBlocking {
        val newsItem = NewsItem(
            status = "ok",
            articles = listOf(Articles(
                title = "News article 1",
                description = "Description of news article 1",
                content = "Content of news article 1",
                author = "Author 1",
                publishedAt = "2022-01-01T00:00:00Z",
                url = "https://www.example.com/article1",
                urlToImage = "https://www.example.com/article1/image.jpg"
            )),
            totalResults = 1
        )
        repository.addArticleToDatabase(newsItem)
    }
}