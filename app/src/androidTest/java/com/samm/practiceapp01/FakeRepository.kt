package com.samm.practiceapp01

import android.util.Log
import com.samm.practiceapp01.data.network.NewsApi
import com.samm.practiceapp01.data.database.NewsDatabase
import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.domain.models.NewsItem
import com.samm.practiceapp01.domain.repository.Repository
import io.mockk.mockk
import org.json.JSONObject
import retrofit2.Response

class FakeRepository(private val db: NewsDatabase): Repository {

    private val api = mockk<NewsApi>()

    override suspend fun fetchArticles(search: String, page: Int): Response<NewsItem> {
        val newsData = api.getNews(
            searchQuery = "Test",
            pageNumber = 1,
            apiKey = "fake key",
            pageSize = 3
        )
        return newsData
    }

    override fun parseErrorBody(response: Response<NewsItem>): String {
        val errorJson = JSONObject(response.errorBody()?.string() ?: "")
        val errorMessage = errorJson.getString("message")
        Log.d("Error Message:", errorMessage)
        return errorMessage
    }
    override suspend fun clearCache() {
        db.myDao().clearCache()
    }

    override suspend fun addArticleToDatabase(articles: List<Articles>?) {
        articles?.forEach { article ->
            db.myDao().insert(article)
        }
    }
}