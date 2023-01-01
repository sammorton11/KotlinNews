package com.samm.practiceapp01.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.samm.practiceapp01.presentation.NewsViewModel

class Utility {

    // Todo: Run an animation when hiding the views
    fun hideViewsWhenScrolled(
        recyclerView: RecyclerView,
        toolbar: MaterialToolbar,
        backToTopButton: FloatingActionButton
    ){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Get the first visible item position
                val firstVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager)
                    .findFirstVisibleItemPosition()

                // Check if the first visible item is the first item in the list
                if (firstVisibleItemPosition == 0) {
                    toolbar.visibility = View.VISIBLE
                    backToTopButton.visibility = View.GONE
                } else {
                    toolbar.visibility = View.GONE
                    backToTopButton.visibility = View.VISIBLE
                }
            }
        })
    }

    fun ifDataIsLoading(
        newsViewModel: NewsViewModel,
        viewLifecycleOwner: LifecycleOwner,
        progressBar: ProgressBar
    ){
        newsViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // Show progress bar
                progressBar.visibility = View.VISIBLE

            } else {
                // Hide progress bar
                progressBar.visibility = View.GONE
            }
        }
    }

    fun hideKeyboard(activity: FragmentActivity?) {
        val view = activity?.currentFocus
        if (view != null) {
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}