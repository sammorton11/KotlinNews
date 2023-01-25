package com.samm.practiceapp01.data.repository

import android.util.Log
import com.samm.practiceapp01.core.Constants.PAGE_SIZE
import com.samm.practiceapp01.data.database.NewsDatabase
import com.samm.practiceapp01.data.network.RetrofitInstance
import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.domain.models.NewsItem
import com.samm.practiceapp01.domain.repository.Repository
import org.json.JSONObject
import retrofit2.Response

class RepositoryImpl (db: NewsDatabase): Repository {

    private val dao = db.myDao()
    val articlesFromDatabase = dao.getAllNewsItems()

    override suspend fun fetchArticles(search: String, page: Int) =
        RetrofitInstance.newsApi.getNews(
            searchQuery = search,
            pageNumber = page,
            pageSize = PAGE_SIZE
        )

    override fun parseErrorBody(response: Response<NewsItem>): String {
        val errorJson = JSONObject(response.errorBody()?.string() ?: "")
        val errorMessage = errorJson.getString("message")
        Log.d("Error Message:", errorMessage)

        return errorMessage
    }

    override suspend fun clearCache() {
        dao.clearCache()
    }

    override suspend fun addArticleToDatabase(articles: List<Articles>?) {
        articles?.forEach { article ->
            dao.insert(article)
        }
    }
}