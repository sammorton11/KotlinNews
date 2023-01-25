package com.samm.practiceapp01

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.samm.practiceapp01.data.database.NewsDao
import com.samm.practiceapp01.data.database.NewsDatabase
import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.domain.models.NewsItem
import com.samm.practiceapp01.domain.repository.Repository
import kotlinx.coroutines.runBlocking
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
    private val newsItem = NewsItem(
        totalResults = 100,
        articles = listOf(
            Articles(
                title = "News article 1",
                description = "Description of news article 1",
                content = "Content of news article 1",
                author = "Author 1",
                publishedAt = "2022-01-01T00:00:00Z",
                url = "https://www.example.com/article1",
                urlToImage = "https://www.example.com/article1/image.jpg"
            ),
            Articles(
                title = "News article 2",
                description = "Description of news article 2",
                content = "Content of news article 2",
                author = "Author 2",
                publishedAt = "2022-01-02T00:00:00Z",
                url = "https://www.example.com/article2",
                urlToImage = "https://www.example.com/article2/image.jpg"
            )
        ),
        status = "ok"
    )

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java).build()
        dao = db.myDao()
        repository = FakeRepository(db)
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        db.close()
    }

    @Test
    fun databaseTest(): Unit = runBlocking {
        repository.addArticleToDatabase(newsItem.articles)
        val result = db.myDao().getAllNewsItems().value
        assert(result!!.isNotEmpty())
    }
}