package com.samm.practiceapp01.domain

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samm.practiceapp01.data.NewsApi
import com.samm.practiceapp01.domain.models.Articles

class Repository(private val api: NewsApi) {

    val articles: MutableLiveData<List<Articles>> = MutableLiveData()
    val status: MutableLiveData<String?> = MutableLiveData()
    val totalResults: MutableLiveData<Int?> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    /*
        ISSUE:
        Using a live data below to check whether there are no results found in the search results.
        The search field is hidden when the first item in the list is not in view,
        if there are no results in the list, then the search field was hidden.

        SOLUTION:
        Using this live data means I don't have to rely on a views state
        to hide or show the search field.
     */
    val noResults: MutableLiveData<Boolean> = MutableLiveData()

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
                noResults.postValue(true)
                Toast.makeText(context, "No results found", Toast.LENGTH_LONG).show()
            }
            // Post the values to the live data
            articles.postValue(articlesResponse)
            status.postValue(statusResponse)
            totalResults.postValue(totalResultsResponse)

        } catch (_: Exception){ }

        loading.postValue(false)
    }
}