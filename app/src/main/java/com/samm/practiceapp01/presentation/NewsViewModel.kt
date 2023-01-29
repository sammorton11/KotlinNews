package com.samm.practiceapp01.presentation

import android.app.Application
import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import com.samm.practiceapp01.NewsState
import com.samm.practiceapp01.data.database.NewsDatabase
import com.samm.practiceapp01.data.repository.RepositoryImpl
import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.domain.models.NewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = NewsDatabase.getDatabase(application)
    private val repository = RepositoryImpl(database)
    private val articlesFromDb = repository.articlesFromDatabase
    private val newsState = MutableLiveData<NewsState>()
    val state = newsState

    // Get the articles form the response and add it to the Database
    fun fetchArticles(search: String, page: Int) = viewModelScope.launch(Dispatchers.IO) {
        getResponseFlow(search, page)
            .catch {
                newsState.postValue(it.message?.let { message -> NewsState(error = message) })
            }
            .collect { result ->
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

    fun getDbFlow(adapter: NewsAdapter) = viewModelScope.launch(Dispatchers.Main) {
        articlesFromDb.collect { list ->
            adapter.setNews(list)
        }
    }

    private fun getResponseFlow(search: String, page: Int)
    : Flow<Resource<Response<NewsItem>>> = flow {
        val list = repository.fetchArticles(search, page)
        when {
            list.isSuccessful -> {
                emit(Resource.Success(list))
            }
            !list.isSuccessful -> {
                emit(Resource.Error(list.message()))
            }
            else -> {
                emit(Resource.Loading())
            }
        }
    }

    fun getState(owner: LifecycleOwner, loadingView: View, adapter: NewsAdapter)
    = viewModelScope.launch(Dispatchers.Main) {

        state.observe(owner, Observer { state ->
            when {
                state.isLoading -> {
                    loadingView.visibility = View.VISIBLE
                }
                state?.articles?.isNotEmpty() == true -> {
                    loadingView.visibility = View.GONE
                    adapter.setNews(state.articles)
                }
                state?.error?.isNotEmpty() == true -> {
                    loadingView.visibility = View.GONE
                }
                else -> {
                    getDbFlow(adapter)
                }
            }
        })
    }


    fun clearCache() {
        viewModelScope.launch(Dispatchers.IO) {
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

    //  Not sure if this is working properly
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