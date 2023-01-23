package com.samm.practiceapp01.domain.repository

import com.samm.practiceapp01.domain.models.NewsItem
import retrofit2.Response

interface Repository {
    suspend fun fetchArticles(search: String, page: Int)
    fun parseErrorBody(response: Response<NewsItem>): String
    suspend fun clearCache()
    // API call
    suspend fun getNews(search: String, page: Int): Response<NewsItem>

    suspend fun addArticleToDatabase(newsItem: NewsItem)

}