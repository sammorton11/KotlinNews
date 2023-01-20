package com.samm.practiceapp01

import com.samm.practiceapp01.data.NewsApi
import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.domain.models.NewsItem
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class ApiConnectionTest {

    private val mockApi = mockk<NewsApi>()
    private val expectedNewsItem = NewsItem(
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

    @Test
    fun  `getNews returns successful response`() {
        coEvery {
            mockApi.getNews(any(), any(), any(), any())
        } returns Response.success(expectedNewsItem)
    }

    @Test
    fun `getNews returns error response`(){
        coEvery {
            val result = mockApi.getNews("", 1, "", 1)
            assertTrue(!result.isSuccessful)
        }
    }
}