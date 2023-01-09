package com.samm.practiceapp01.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.samm.practiceapp01.data.RepositoryImpl
import com.samm.practiceapp01.data.RetrofitInstance
import com.samm.practiceapp01.data.database.NewsDatabase
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = NewsDatabase.getDatabase(application).myDao()
    private val repositoryImpl = RepositoryImpl(RetrofitInstance.newsApi, dao)

    val loading: MutableLiveData<Boolean> = repositoryImpl.loading
    val error: MutableLiveData<String> = repositoryImpl.errorMessageLD
    val noResults: MutableLiveData<Boolean> = repositoryImpl.noResults

    private fun fetchArticles(search: String, page: Int) = viewModelScope.launch {
        repositoryImpl.fetchArticles(search, page)
    }

    fun clearCache() = viewModelScope.launch { repositoryImpl.clearCache() }
    fun getArticles(page: Int, search: String) { fetchArticles(search, page) }

    fun newsData(owner: LifecycleOwner, adapter: NewsAdapter){
        val newsList = repositoryImpl.getNewsFromDatabase
        newsList.observe(owner){ list ->
            when {
                list.isEmpty() -> noResults.postValue(true)
                else -> adapter.setNews(list)
            }
        }
    }
}