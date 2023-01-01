package com.samm.practiceapp01.presentation

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samm.practiceapp01.RetrofitInstance
import com.samm.practiceapp01.domain.Repository
import com.samm.practiceapp01.domain.models.Articles
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val repository = Repository(RetrofitInstance.newsApi)

    val articles: MutableLiveData<List<Articles>> = repository.articles
    val totalResults: MutableLiveData<Int?> = repository.totalResults
    val loading: MutableLiveData<Boolean> = repository.loading

    val noResults: MutableLiveData<Boolean> = repository.noResults


    private fun fetchArticles(search: String, context: Context) = viewModelScope.launch {
        repository.fetchArticles(search, context)
    }

    fun getArticles(
        searchField: EditText,
        resultsLayout: LinearLayout,
        context: Context
    ) {
        val search = searchField.text.toString()
        if (search.isBlank()){
            Toast.makeText(
                context, "Please enter a search term",
                Toast.LENGTH_LONG
            ).show()
        } else {
            context.let {
                fetchArticles(search, it)
                resultsLayout.visibility = View.VISIBLE
            }
        }
    }

}