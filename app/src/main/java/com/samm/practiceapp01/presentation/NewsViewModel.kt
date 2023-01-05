package com.samm.practiceapp01.presentation

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samm.practiceapp01.data.RetrofitInstance
import com.samm.practiceapp01.data.RepositoryImpl
import com.samm.practiceapp01.domain.models.Articles
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val repositoryImpl = RepositoryImpl(RetrofitInstance.newsApi)

    val articles: MutableLiveData<List<Articles>> = repositoryImpl.articles
    val totalResults: MutableLiveData<Int?> = repositoryImpl.totalResults
    val loading: MutableLiveData<Boolean> = repositoryImpl.loading
    val error: MutableLiveData<String> = repositoryImpl.errorMessageLD

    val noResults: MutableLiveData<Boolean> = repositoryImpl.noResults


    private fun fetchArticles(search: String, page: Int, context: Context) = viewModelScope.launch {
        repositoryImpl.fetchArticles(search, page, context)
    }

    fun getArticles(
        resultsLayout: LinearLayout,
        page: Int,
        search: String,
        context: Context
    ) {
        if (search.isBlank()){
            Toast.makeText(
                context, "Please enter a search term",
                Toast.LENGTH_LONG
            ).show()
        } else {
            context.let {
                fetchArticles(search, page, it)
                resultsLayout.visibility = View.VISIBLE
            }
        }
    }

}