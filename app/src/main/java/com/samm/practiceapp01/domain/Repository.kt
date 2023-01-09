package com.samm.practiceapp01.domain

import com.samm.practiceapp01.domain.models.NewsItem
import retrofit2.Response

interface Repository {
    suspend fun fetchArticles(search: String, page: Int)
    fun parseErrorBody(response: Response<NewsItem>): String
}