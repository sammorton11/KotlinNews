package com.samm.practiceapp01.presentation

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
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
    private val state = newsState as LiveData<NewsState>

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
                            val removeDuplicates = removeDuplicates(list)
                            newsState.postValue(NewsState(articles = removeDuplicates))
                            clearCache()
                            repository.addArticleToDatabase(removeDuplicates)
                        }
                    }
                    is Resource.Error -> {
                        newsState.postValue(result.message?.let { NewsState(error = it) })
                    }
                }
            }
    }

    fun getDbFlow(adapter: NewsAdapter, recyclerView: RecyclerView) = viewModelScope.launch(Dispatchers.Main) {
        articlesFromDb.collect { list ->
            recyclerView.scheduleLayoutAnimation()
            adapter.setNews(list)
        }
    }
    private fun getResponseFlow(search: String, page: Int): Flow<Resource<Response<NewsItem>>> = flow {
        emit(Resource.Loading())
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

    fun getState(owner: LifecycleOwner, loadingView: View, errorView: TextView, adapter: NewsAdapter, recyclerView: RecyclerView)
        = viewModelScope.launch(Dispatchers.Main) {

        state.observe(owner, Observer { state ->
            when {
                state.isLoading -> {
                    loadingView.visibility = View.VISIBLE
                }
                state?.articles?.isNotEmpty() == true -> {
                    loadingView.visibility = View.GONE
                    recyclerView.scheduleLayoutAnimation()
                    adapter.setNews(state.articles)
                }
                state?.error?.isNotEmpty() == true -> {
                    loadingView.visibility = View.GONE
                    errorView.visibility = View.VISIBLE
                    errorView.text = state.error
                }
                else -> {
                    getDbFlow(adapter, recyclerView)
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

    /*
        - checking for a duplicate
        - for some reason the duplicate cards were "unique" so casting to a set did not work.
        - This is my workaround.
        - Don't make this private -- needed in tests
     */
    fun removeDuplicates(list: List<Articles>): List<Articles> {
        val newList = mutableListOf<Articles>()
        list.forEach {
            newList.add(it)
            for (i in newList.indices ) {
                for (j in i+1 until newList.size){
                    if (newList[i].title == newList[j].title) {
                        newList.removeAt(j)
                    }
                }

            }
        }
        return newList.toList()
    }
}