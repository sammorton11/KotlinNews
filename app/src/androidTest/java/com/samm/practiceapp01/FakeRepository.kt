package com.samm.practiceapp01

import com.samm.practiceapp01.data.NewsApi
import com.samm.practiceapp01.data.database.NewsDao
import com.samm.practiceapp01.domain.Repository
import com.samm.practiceapp01.domain.models.NewsItem
import retrofit2.Response

class FakeRepository(private val api: NewsApi, private val myDao: NewsDao): Repository {

    override suspend fun fetchArticles(search: String, page: Int) {
        val newsData = getNews("", 1)
        clearCache()
        newsData.body()?.let { addArticleToDatabase(it) }

    }

    override fun parseErrorBody(response: Response<NewsItem>): String {
        TODO("Not yet implemented")
    }

    override suspend fun clearCache() {
        myDao.clearCache()
    }

    override suspend fun getNews(search: String, page: Int): Response<NewsItem> {
        return api.getNews(
            searchQuery = "Test",
            pageNumber = 1,
            apiKey = "fake key",
            pageSize = 3
        )
    }

    override suspend fun addArticleToDatabase(newsItem: NewsItem) {
        newsItem.articles.forEach { article ->
            myDao.insert(article)
        }
    }
}