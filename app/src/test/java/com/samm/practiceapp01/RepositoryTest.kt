package com.samm.practiceapp01

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.samm.practiceapp01.data.NewsApi
import com.samm.practiceapp01.domain.Repository
import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.domain.models.NewsItem
import com.samm.practiceapp01.domain.models.Source
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Response


/*
    Todo:
        - Need to mock Toast class in tests
            -- getting error: Method makeText in android.widget.Toast not mocked.
 */

class RepositoryTest {


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

//    @Before
//    fun setUp() {
//        val mockLooper = mock(Looper::class.java)
//        `when`(Looper.getMainLooper()).thenReturn(mockLooper)
//    }

    @Test
    fun `fetch articles with valid search query`() {
        val search = "test"
        val api = mockk<NewsApi>()
        val source = mockk<Source>()
        val author = "Test Author"
        val title = "Test Title"
        val description: String = ""
        val url: String = ""
        val urlToImage: String = ""
        val publishedAt: String = ""
        val content: String = ""

        val context = mockk<Context>()
        val articles = listOf(Articles(
            source,
            author,
            title,
            description,
            url,
            urlToImage,
            publishedAt,
            content
        ))
        val status = "ok"
        val totalResults = 1
        val response = NewsItem(status, totalResults, articles)

        every {
            runBlocking {
                api.getNews(searchQuery = search)
            }
        } returns Response.success(response)

        val repository = Repository(api)

        runBlocking {
            repository.fetchArticles(search, context)
        }

        assertEquals(articles, repository.articles.value)
        assertEquals(status, repository.status.value)
        assertEquals(totalResults, repository.totalResults.value)
    }

    @Test
    fun `fetch articles with no results`() {
        val search = "test"
        val api = mockk<NewsApi>()
        val context = mockk<Context>()
        val articles = emptyList<Articles>()
        val status = "ok"
        val totalResults = 0
        val response = NewsItem(status, totalResults, articles)


        every {
            runBlocking {
                api.getNews(searchQuery = search)
            }
        } returns Response.success(response)

        val repository = Repository(api)

        runBlocking {
            repository.fetchArticles(search, context)
        }

        verify { println("No results found") }

        assertEquals(articles, repository.articles.value)
        assertEquals(status, repository.status.value)
        assertEquals(totalResults, repository.totalResults.value)
    }

    @Test
    fun `fetch articles with error`() {
        val search = "News"
        val api = mockk<NewsApi>()
        val context = mockk<Context>()
        val exception = Exception()

        every {
            runBlocking {
                api.getNews(searchQuery = search)
            }

        } throws exception

        val repository = Repository(api)

        runBlocking {
            repository.fetchArticles(search, context)
        }

        assertNull(repository.articles.value)
        assertNull(repository.status.value)
        assertNull(repository.totalResults.value)

    }
}