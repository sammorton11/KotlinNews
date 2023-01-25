package com.samm.practiceapp01.domain.repository

import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.domain.models.NewsItem
import retrofit2.Response

interface Repository {
    suspend fun fetchArticles(search: String, page: Int): Response<NewsItem>
    fun parseErrorBody(response: Response<NewsItem>): String
    suspend fun clearCache()
    suspend fun addArticleToDatabase(articles: List<Articles>?)
}