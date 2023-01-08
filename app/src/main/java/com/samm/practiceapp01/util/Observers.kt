package com.samm.practiceapp01.util

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.samm.practiceapp01.presentation.NewsAdapter
import com.samm.practiceapp01.presentation.NewsViewModel


class Observers(
    private val newsViewModel: NewsViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val context: Context
) {

    /*
        Observer - Using the 'articles' live data list from the view model and observing it.
        Then apply that list to the adapter to set the data to the Recycler View
     */
    private fun observer(adapter: NewsAdapter) {
        newsViewModel.articles.observe(viewLifecycleOwner){ listFromViewModel ->
            try {
                if (listFromViewModel != null) {
                    adapter.setNews(listFromViewModel)
                }
            }
            catch (e: Exception){
                Log.d("ERROR", "$e")
            }
        }
    }

    // Need this so the search bar is not hidden when there are no results
    private fun noResultsObserver(view: View) {
        newsViewModel.noResults.observe(viewLifecycleOwner){
            if (it == true){
                view.visibility = View.VISIBLE
                Toast.makeText(this.context, "No results found", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun allLiveDataObservers(searchBar: View, adapter: NewsAdapter) {
        this.noResultsObserver(searchBar)
        this.observer(adapter)
    }
}

fun getPageAmount(total: Int, pageSize: Int): Int {
    return (total + pageSize - 1) / pageSize
}