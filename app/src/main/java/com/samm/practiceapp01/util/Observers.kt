package com.samm.practiceapp01.util

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.samm.practiceapp01.presentation.NewsAdapter
import com.samm.practiceapp01.presentation.NewsViewModel
import com.samm.practiceapp01.util.Constants.pageAmount


class Observers(
    private val newsViewModel: NewsViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) {

    /*
        Observer - Using the 'articles' live data list from the view model and observing it.
        Then apply that list to the adapter to set the data to the Recycler View
     */
    private fun observer(adapter: NewsAdapter) {
        newsViewModel.articles.observe(viewLifecycleOwner){ listFromViewModel ->
            try {
                adapter.setNews(listFromViewModel)
            }
            catch (e: Exception){
                Log.d("ERROR", "$e")
            }
        }
    }

    private fun totalResultsObserver(resultsAmount: TextView){
        // were just using the results amount label to test the status data
        newsViewModel.totalResults.observe(viewLifecycleOwner){ total ->
            if (total != null) {
                pageAmount = total
                resultsAmount.text = total.toString()
            }
        }
    }

    private fun noResultsObserver(view: View){
        newsViewModel.noResults.observe(viewLifecycleOwner){
            if (it == true){
                view.visibility = View.VISIBLE
            }
        }
    }

    fun allLiveDataObservers(view: TextView, toolbar: View, adapter: NewsAdapter) {
        this.noResultsObserver(toolbar)
        this.totalResultsObserver(view)
        this.observer(adapter)
    }
}

fun getPageAmount(total: Int, pageSize: Int): Int {
    Log.d("Page Amount", "${((total / pageSize) / 2)}")
    return ((total / pageSize) / 2)
}