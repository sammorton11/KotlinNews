package com.samm.practiceapp01

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samm.practiceapp01.data.NewsApi
import com.samm.practiceapp01.models.Articles
import com.samm.practiceapp01.models.NewsItem

class Repository(private val api: NewsApi) {

    val articles: MutableLiveData<List<Articles>> = MutableLiveData()
    val status: MutableLiveData<String?> = MutableLiveData()
    val totalResults: MutableLiveData<Int?> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    suspend fun fetchArticles(search: String, context: Context) {

        loading.postValue(true)

        try {
            // Get the response from the get request
            val response = api.getNews(searchQuery = search)

            // Get the data from the response
            val articlesResponse = response.body()?.articles
            val statusResponse = response.body()?.status
            val totalResultsResponse = response.body()?.totalResults

            // check if there were no results for the search
            if (articlesResponse!!.isEmpty()){
                Toast.makeText(context, "No results found", Toast.LENGTH_LONG).show()
            }
            // Post the values to the live data
            this.articles.postValue(articlesResponse)
            this.status.postValue(statusResponse)
            this.totalResults.postValue(totalResultsResponse)

        } catch (e: Exception){
            Toast.makeText(context, "Error: $e", Toast.LENGTH_LONG).show()
        }

        loading.postValue(false)
    }
}