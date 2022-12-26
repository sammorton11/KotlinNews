package com.samm.practiceapp01

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.samm.practiceapp01.data.NewsApi
import com.samm.practiceapp01.models.Articles
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsViewModel : ViewModel() {

    private val repository = Repository(RetrofitInstance.newsApi)

    val articles: MutableLiveData<List<Articles>> = repository.articles
    val status: MutableLiveData<String?> = repository.status
    val totalResults: MutableLiveData<Int?> = repository.totalResults
    val loading: MutableLiveData<Boolean> = repository.loading


    fun fetchArticles(search: String, context: Context) = viewModelScope.launch {
        repository.fetchArticles(search, context)
    }
}