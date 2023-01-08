package com.samm.practiceapp01.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samm.practiceapp01.data.database.NewsDao
import com.samm.practiceapp01.domain.Repository
import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.domain.models.NewsItem
import com.samm.practiceapp01.util.Constants.PAGE_SIZE
import org.json.JSONObject
import retrofit2.Response

class RepositoryImpl (private val api: NewsApi, private val myDao: NewsDao): Repository {

    val articles: MutableLiveData<List<Articles>?> = MutableLiveData()
    val totalResults: MutableLiveData<Int?> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessageLD: MutableLiveData<String> = MutableLiveData()
    val noResults: MutableLiveData<Boolean> = MutableLiveData()

    val getNewsFromDatabase = myDao.getAllNewsItems()

    fun getPage(limit: Int, offset: Int): LiveData<List<Articles>>{
        return myDao.getPage(limit, offset)
    }

    suspend fun clearCache() {
        myDao.refreshCache()
    }
    override suspend fun fetchArticles(search: String, page: Int) {
        loading.postValue(true)

        try {
            clearCache()
            val response = getNews(search, page)

            if (response.isSuccessful){
                myDao.refreshCache()
//                updateLiveDataFromResponse(response.body())
                response.body()?.let { newsItem ->
                    newsItem.articles.forEach { article ->
                        myDao.insert(article)
                    }

                }
            } else {
                val parsedErrorResponse = parseErrorBody(response)
                errorMessageLD.postValue(parsedErrorResponse)
                Log.d("Error Response", parsedErrorResponse)
            }

            Log.d("Is Successful;", "${response.isSuccessful}")
            Log.d("Response", "${response.body()?.articles}")
        } catch (e: Exception){
            Log.d("Exception:", "$e")
        }
        loading.postValue(false)
    }

    // API call
    private suspend fun getNews(search: String, page: Int): Response<NewsItem> {
        return api.getNews(searchQuery = search, pageNumber = page, pageSize = PAGE_SIZE)
    }

    // parse the error body from the API response
    override fun parseErrorBody(response: Response<NewsItem>): String {
        val errorJson = JSONObject(response.errorBody()?.string() ?: "")
        val errorMessage = errorJson.getString("message")
        Log.d("Error Message:", errorMessage)
        return errorMessage
    }
}