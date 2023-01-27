package com.samm.practiceapp01.presentation

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import com.samm.practiceapp01.NewsState
import com.samm.practiceapp01.data.database.NewsDatabase
import com.samm.practiceapp01.data.repository.RepositoryImpl
import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.domain.models.NewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Response
/*
    Todo:
        - check if the cached data is present before making the API call, and if so, use that data instead of making the API call.
 */

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = NewsDatabase.getDatabase(application)
    private val repository = RepositoryImpl(database)
    val articlesFromDb = repository.articlesFromDatabase
    private val newsState = MutableLiveData<NewsState>()
    val _newsState = newsState

    // Get the articles form the response and add it to the Database
    fun fetchArticles(search: String, page: Int) = viewModelScope.launch(Dispatchers.IO) {
        val flow = flow(search, page)

        flow.collect { result ->

            when (result) {
                is Resource.Loading -> {
                    newsState.postValue(NewsState(isLoading = true))
                }
                is Resource.Success -> {
                    result.data?.body()?.articles?.let { list ->
                        newsState.postValue(NewsState(articles = list))
                        clearCache()
                        val removeDuplicates = removeDuplicates(list)
                        repository.addArticleToDatabase(removeDuplicates)
                    }
                }
                is Resource.Error -> {
                    newsState.postValue(result.message?.let { NewsState(error = it) })
                }
            }
        }
    }

    private fun flow(search: String, page: Int): Flow<Resource<Response<NewsItem>>> = flow {
        emit(Resource.Loading())
        val list = repository.fetchArticles(search, page)
        emit(Resource.Success(list))
    }


    fun clearCache() {
        viewModelScope.launch(Dispatchers.Main) {
            repository.clearCache()
        }
    }

    fun deleteAllAlertDialog(
        context: Context,
        deleteAll: () -> Unit
    ){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("Are you sure you want to clear the list?")
            .setCancelable(false)
            .setPositiveButton("Clear") { dialog, _ ->
                deleteAll()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Warning")
        alert.show()
    }


    private fun removeDuplicates(list: List<Articles>): List<Articles> {
        list.forEach { articles ->
            println(articles)
        }
        val set = list.toSet()
        set.forEach {
            println(it)
        }
        return set.toList()
    }
}