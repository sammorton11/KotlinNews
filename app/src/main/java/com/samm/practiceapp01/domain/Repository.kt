package com.samm.practiceapp01.domain

import android.content.Context
import com.samm.practiceapp01.domain.models.NewsItem
import retrofit2.Response

interface Repository {
    suspend fun fetchArticles(search: String, page: Int, context: Context)
    fun parseErrorBody(response: Response<NewsItem>): String
}