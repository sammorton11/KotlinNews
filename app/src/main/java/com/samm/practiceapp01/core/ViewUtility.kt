package com.samm.practiceapp01.core

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isEmpty
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class ViewUtility {
    fun hideKeyboard(activity: FragmentActivity?) {
        val view = activity?.currentFocus
        if (view != null) {
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    fun formatDate(input: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("MM-dd-yyyy - hh:mm a", Locale.US)
        val date = inputFormat.parse(input)

        return date?.let { outputFormat.format(it) }
    }

    fun scrollToTop(recyclerView: RecyclerView, view: View) {
        recyclerView.smoothScrollToPosition(0)
        view.visibility = View.GONE
    }

    fun <T:RecyclerView.Adapter<*>> scrollToBottom(recyclerView: RecyclerView, adapter: T) {
        recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
    }

    fun hideViewsWhenScrolled(
        recyclerView: RecyclerView,
        toolbar: View? = null,
        backToTopButton: FloatingActionButton? = null
    ){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Get the first visible item position
                val firstVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager)
                    .findFirstVisibleItemPosition()

                // Check if the first visible item is the first item in the list
                if (firstVisibleItemPosition == 0 || recyclerView.isEmpty()) {
                    toolbar?.visibility = View.VISIBLE
                    backToTopButton?.visibility = View.GONE
                } else {
                    toolbar?.visibility = View.GONE
                    backToTopButton?.visibility = View.VISIBLE
                }
            }
        })
    }
}
