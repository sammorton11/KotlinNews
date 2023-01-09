package com.samm.practiceapp01.data

import com.samm.practiceapp01.domain.models.NewsItem
import com.samm.practiceapp01.util.Constants.API_KEY
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
        apiKey: String = API_KEY,
        @Query("pageSize")
        pageSize: Int
    ): Response<NewsItem>
}