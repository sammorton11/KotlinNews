package com.samm.practiceapp01.presentation

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.samm.practiceapp01.data.RepositoryImpl
import com.samm.practiceapp01.data.RetrofitInstance
import com.samm.practiceapp01.data.database.NewsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = NewsDatabase.getDatabase(application).myDao()
    private val repositoryImpl = RepositoryImpl(RetrofitInstance.newsApi, dao)

    val loading: MutableLiveData<Boolean> = repositoryImpl.loading
    val error: MutableLiveData<String> = repositoryImpl.errorMessageLD

    private fun fetchArticles(search: String, page: Int) = viewModelScope.launch(Dispatchers.IO) {
        repositoryImpl.fetchArticles(search, page)
    }
    fun getArticles(page: Int, search: String) {
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