package com.samm.practiceapp01.data

import com.samm.practiceapp01.models.NewsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("/v2/everything")
    suspend fun getNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = "9ea6d6be1e6d41d2a86042774ef87848"
    ): Response<NewsItem>
}