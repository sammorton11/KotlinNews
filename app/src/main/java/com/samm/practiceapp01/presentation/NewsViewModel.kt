package com.samm.practiceapp01.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.samm.practiceapp01.data.database.NewsDatabase
import com.samm.practiceapp01.data.RetrofitInstance
import com.samm.practiceapp01.data.RepositoryImpl
import com.samm.practiceapp01.domain.models.Articles
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = NewsDatabase.getDatabase(application).myDao()
    private val repositoryImpl = RepositoryImpl(RetrofitInstance.newsApi, dao)


    val articles: MutableLiveData<List<Articles>?> = repositoryImpl.articles
    val totalResults: MutableLiveData<Int?> = repositoryImpl.totalResults
    val loading: MutableLiveData<Boolean> = repositoryImpl.loading
    val error: MutableLiveData<String> = repositoryImpl.errorMessageLD

    val noResults: MutableLiveData<Boolean> = repositoryImpl.noResults

    private fun fetchArticles(search: String, page: Int) = viewModelScope.launch {
        repositoryImpl.fetchArticles(search, page)
    }

    fun refreshCache() = viewModelScope.launch {
        repositoryImpl.clearCache()
    }

    fun newsData(owner: LifecycleOwner, adapter: NewsAdapter){
        val newsList = repositoryImpl.getNewsFromDatabase
        newsList.observe(owner){
            it.forEach {
                Log.d("LIST", "$it")
            }
            adapter.setNews(it)
        }
    }
    fun newsDataByPage(
        limit: Int,
        offset: Int,
        owner: LifecycleOwner,
        adapter: NewsAdapter
    ){
        val page = repositoryImpl.getPage(limit, offset)
        page.observe(owner){
            adapter.setNews(it)
        }
    }

    fun getArticles(
        page: Int,
        search: String
    ){
        fetchArticles(search, page)
    }
}