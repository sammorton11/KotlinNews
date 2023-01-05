package com.samm.practiceapp01.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.presentation.NewsAdapter
import com.samm.practiceapp01.presentation.NewsViewModel
import java.text.SimpleDateFormat
import java.util.*

class Utility {

    // Todo: Run an animation when hiding the views
    fun hideViewsWhenScrolled(
        resultsAmount: TextView,
        recyclerView: RecyclerView,
        toolbar: View,
        backToTopButton: FloatingActionButton
    ){
        if (!resultsAmount.text.contains("0")){
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


    // Load an image into the image view in the news card
    fun loadNewsImage(
        holder: NewsAdapter.ViewHolder,
        newsItem: Articles,
        width: Int,
        height: Int
    ){
        val imageUrl: String = newsItem.urlToImage
        val newsImage: ImageView = holder.newsImage
        val context: Context = holder.newsImage.context

        Glide.with(context)
            .load(imageUrl)
            .override(width, height) // resizing
            .centerCrop()
            .into(newsImage)
    }


    fun formatDate(input: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("MM-dd-yyyy - hh:mm a", Locale.US)
        val date = inputFormat.parse(input)

        return date?.let { outputFormat.format(it) }
    }
}