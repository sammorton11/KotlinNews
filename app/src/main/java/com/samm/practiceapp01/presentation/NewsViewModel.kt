package com.samm.practiceapp01.presentation

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import com.samm.practiceapp01.data.database.NewsDatabase
import com.samm.practiceapp01.data.repository.RepositoryImpl
import com.samm.practiceapp01.domain.models.Articles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = NewsDatabase.getDatabase(application)
    private val repository = RepositoryImpl(database)
    private val newsState = MutableLiveData<NewsDataState<List<Articles>>>()
    private val _newsState = newsState

    // Get the articles form the response and add it to the Database
    fun fetchArticles(search: String, page: Int) = viewModelScope.launch(Dispatchers.IO) {
        newsState.postValue(NewsDataState.Loading())
        try {
            val response = repository.fetchArticles(search, page)
            if (response.isSuccessful) {
                clearCache()
                response.body()?.articles?.let {
                    newsState.postValue(NewsDataState.Success(it))
                    repository.addArticleToDatabase(it)
                }
            }
        } catch (e: HttpException) {
            newsState.postValue(NewsDataState.Error("$e"))
        }
    }

    // Is the state loading, successful, or throwing an error
    fun getState(owner: LifecycleOwner, adapter: NewsAdapter, loadingView: View, context: Context) {
        // Observe State
        _newsState.observe(owner, Observer { state ->
            when (state) {
                is NewsDataState.Loading -> loadingView.visibility = View.VISIBLE
                is NewsDataState.Success -> state.data?.let { list ->

                    if (list.isEmpty()) {
                        setNewsFromDatabase(owner, adapter) // cached data
                    } else {
                        adapter.setNews(list) // response data
                    }

                    loadingView.visibility = View.GONE
                }
                is NewsDataState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                    loadingView.visibility = View.GONE
                }
                else -> {
                    loadingView.visibility = View.GONE
                }
            }
        })
    }

    fun clearCache() {
        viewModelScope.launch(Dispatchers.Main) {
            repository.clearCache()
        }
    }

    fun setNewsFromDatabase(owner: LifecycleOwner, adapter: NewsAdapter) {
        repository.articlesFromDatabase.observe(owner, Observer { listFromDb ->
            adapter.setNews(listFromDb)
        })
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
}