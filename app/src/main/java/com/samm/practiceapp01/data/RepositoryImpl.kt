package com.samm.practiceapp01.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samm.practiceapp01.domain.Repository
import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.domain.models.NewsItem
import com.samm.practiceapp01.util.Constants.PAGE_SIZE
import org.json.JSONObject
import retrofit2.Response

class RepositoryImpl (private val api: NewsApi): Repository {

    val articles: MutableLiveData<List<Articles>> = MutableLiveData()
    val totalResults: MutableLiveData<Int?> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessageLD: MutableLiveData<String> = MutableLiveData()
    val noResults: MutableLiveData<Boolean> = MutableLiveData()

    override suspend fun fetchArticles(search: String, page: Int, context: Context) {
        loading.postValue(true)

        try {

            val response = getNews(search, page)
            if (response.isSuccessful){
                updateLiveDataFromResponse(response.body())
            } else {
                val parsedErrorResponse = parseErrorBody(response)
                errorMessageLD.postValue(parsedErrorResponse)
                Toast.makeText(context, parsedErrorResponse, Toast.LENGTH_SHORT).show()
            }

            Log.d("Is Successful;", "${response.isSuccessful}")
            Log.d("Response", "${response.body()?.articles}")
        } catch (e: Exception){
            Log.d("Exception:", "$e")
        }
        loading.postValue(false)
    }

    // Function to make the API call
    private suspend fun getNews(search: String, page: Int): Response<NewsItem> {
        return api.getNews(searchQuery = search, pageNumber = page, pageSize = PAGE_SIZE)
    }

    // Function to update the LiveData values from the API response
    private fun updateLiveDataFromResponse(response: NewsItem?) {
        val articlesResponse = response?.articles
        val totalResultsResponse = response?.totalResults
        totalResults.postValue(totalResultsResponse)
        articles.postValue(articlesResponse)
        Log.d("Articles", "${articles.value?.first()}")
        if (articlesResponse?.isEmpty() == true){
            noResults.postValue(true)
        }
    }

    // Function to parse the error body from the API response
    override fun parseErrorBody(response: Response<NewsItem>): String {
        val errorJson = JSONObject(response.errorBody()?.string() ?: "")
        val errorMessage = errorJson.getString("message")
        Log.d("Error Message:", errorMessage)
        return errorMessage
    }
}