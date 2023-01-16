package com.samm.practiceapp01.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.samm.practiceapp01.data.database.NewsDao
import com.samm.practiceapp01.domain.Repository
import com.samm.practiceapp01.domain.models.NewsItem
import com.samm.practiceapp01.core.Constants.PAGE_SIZE
import org.json.JSONObject
import retrofit2.Response

class RepositoryImpl (private val api: NewsApi, private val myDao: NewsDao): Repository {

    private var errorString: String = ""
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessageLD: MutableLiveData<String> = MutableLiveData()
    val getNewsFromDatabase = myDao.getAllNewsItems()

    override suspend fun clearCache() {
        myDao.clearCache()
    }
    override suspend fun fetchArticles(search: String, page: Int) {
        loading.postValue(true)
        try {
            val response = getNews(search, page)
            when {
                response.isSuccessful -> {
                    response.body()?.let { newsItem ->
                        clearCache()
                        addArticleToDatabase(newsItem)
                    }
                }
                response.errorBody() != null -> {
                    val parsedErrorResponse = parseErrorBody(response)
                    errorString = parsedErrorResponse
                    errorMessageLD.postValue(errorString)
                }
            }
        } catch (e: Exception){
            errorMessageLD.postValue(errorString)
            Log.d("Exception:", "$e")
        }
        loading.postValue(false)
    }

    override suspend fun getNews(search: String, page: Int): Response<NewsItem> {
        return api.getNews(
            searchQuery = search,
            pageNumber = page,
            pageSize = PAGE_SIZE
        )
    }

    override suspend fun addArticleToDatabase(newsItem: NewsItem){
        newsItem.articles.forEach { article ->
            myDao.insert(article)
        }
    }

    override fun parseErrorBody(response: Response<NewsItem>): String {
        val errorJson = JSONObject(response.errorBody()?.string() ?: "")
        val errorMessage = errorJson.getString("message")
        Log.d("Error Message:", errorMessage)
        return errorMessage
    }
}