package com.samm.practiceapp01.presentation

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import com.samm.practiceapp01.data.repository.RepositoryImpl
import com.samm.practiceapp01.data.network.RetrofitInstance
import com.samm.practiceapp01.data.database.NewsDatabase
import com.samm.practiceapp01.domain.models.Articles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = NewsDatabase.getDatabase(application).myDao()
    private val repositoryImpl = RepositoryImpl(RetrofitInstance.newsApi, dao)

    private val error: MutableLiveData<String> = MutableLiveData()

    private val newsState = MutableLiveData<NewsDataState<List<Articles>>>()
    val _newsState = newsState

    private fun fetchArticles(search: String, page: Int) = viewModelScope.launch(Dispatchers.IO) {
        newsState.postValue(NewsDataState.Loading())
        repositoryImpl.fetchArticles(search, page)
        val articles = repositoryImpl.getNewsFromDatabase.value

        if(articles != null)
            newsState.postValue(NewsDataState.Success(articles))
        else
            newsState.postValue(NewsDataState.Error(error.value))
    }

    fun getArticles(search: String, page: Int) {
        fetchArticles(search, page)
    }

    fun clearCache() {
        viewModelScope.launch(Dispatchers.Main) {
            repositoryImpl.clearCache()
        }
    }

    fun newsData(owner: LifecycleOwner, adapter: NewsAdapter){
        val newsList = repositoryImpl.getNewsFromDatabase
        newsList.observe(owner){ list ->
            adapter.setNews(list)
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
}