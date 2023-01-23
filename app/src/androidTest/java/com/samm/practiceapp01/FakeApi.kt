package com.samm.practiceapp01

import com.samm.practiceapp01.data.network.NewsApi
import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.domain.models.NewsItem
import retrofit2.Response

object FakeNewsApi : NewsApi {

    private val fakeArticle1 = Articles(
        author = "John",
        title = "Test Title 1",
        description = "Test Description 1",
        url = "test Url 1",
        urlToImage = "test Url to image 1",
        publishedAt = "2015-12-12T00:00:00",
        content = "Test content 1"
    )
    private val fakeArticle2 = Articles(
        author = "John",
        title = "Test Title 1",
        description = "Test Description 1",
        url = "test Url 1",
        urlToImage = "test Url to image 1",
        publishedAt = "2015-12-12T00:00:00",
        content = "Test content 1"
    )
    private val fakeArticle3 = Articles(
        author = "John",
        title = "Test Title 1",
        description = "Test Description 1",
        url = "test Url 1",
        urlToImage = "test Url to image 1",
        publishedAt = "2015-12-12T00:00:00",
        content = "Test content 1"
    )

    private const val statusSuccess = "ok"
    private val articlesSuccess = listOf(fakeArticle1, fakeArticle2, fakeArticle3)
    private val totalResultsSuccess = articlesSuccess.size

    private val fakeNewsItemSuccess = NewsItem(
        status = statusSuccess,
        totalResults = totalResultsSuccess,
        articles = articlesSuccess
    )
    override suspend fun getNews(
        searchQuery: String,
        pageNumber: Int,
        apiKey: String,
        pageSize: Int,
    ): Response<NewsItem> {
        return Response.success(fakeNewsItemSuccess)
    }
}