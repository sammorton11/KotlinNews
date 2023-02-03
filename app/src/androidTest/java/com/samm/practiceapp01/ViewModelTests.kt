package com.samm.practiceapp01

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.samm.practiceapp01.data.database.NewsDao
import com.samm.practiceapp01.data.database.NewsDatabase
import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.domain.models.NewsItem
import com.samm.practiceapp01.presentation.NewsViewModel
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

// In-Progress
@RunWith(AndroidJUnit4::class)
class ViewModelTests {

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var mockViewModel: NewsViewModel
    private lateinit var db: NewsDatabase
    private lateinit var dao: NewsDao

    private val duplicateNewsItemList = NewsItem(
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
                title = "News article 1",
                description = "Description of news article 1",
                content = "Content of news article 1",
                author = "Author 1",
                publishedAt = "2022-01-01T00:00:00Z",
                url = "https://www.example.com/article1",
                urlToImage = "https://www.example.com/article1/image.jpg"
            )
        ),
        status = "ok"
    )

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java).build()
        dao = db.myDao()
        mockViewModel = mockk()
        dao.clearCache()
        duplicateNewsItemList.articles.forEach { article ->
            dao.insert(article)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() = runBlocking {
        dao.clearCache()
        db.close()
    }

    /*
        Todo:
            - removeDuplicates() throws error in test
     */
    @Test
    fun test_empty_database() = runBlocking {
        dao.getAllNewsItems().collect { list ->
            //val newList = mockViewModel.removeDuplicates(list)
            assert(list.size == 2)
        }
    }
}
