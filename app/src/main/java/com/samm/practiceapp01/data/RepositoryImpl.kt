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
            val response = api.getNews(searchQuery = search, pageNumber = page, pageSize = PAGE_SIZE)

            // Get error message if error
            val errorMessage = parseErrorBody(response)
            errorMessageLD.postValue(errorMessage)

            // Objects from successful response assigned to Live Data values
            val articlesResponse = response.body()?.articles
            val totalResultsResponse = response.body()?.totalResults
            articles.postValue(articlesResponse)
            totalResults.postValue(totalResultsResponse)

            // No results found
            if (articlesResponse!!.isEmpty()){
                noResults.postValue(true)
                Toast.makeText(context, "No results found", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception){
            Log.d("Exception:", "$e")
        }
        loading.postValue(false)
    }

    // Function to parse the error body from the API response
    override fun parseErrorBody(response: Response<NewsItem>): String {
        val errorJson = JSONObject(response.errorBody()?.string() ?: "")
        val errorMessage = errorJson.getString("message")
        Log.d("Error Message:", errorMessage)
        return errorMessage
    }
}