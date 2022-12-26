package com.samm.practiceapp01

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ArticleFragment : Fragment() {

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchField: EditText
    private lateinit var searchIconButton: ImageButton
    private lateinit var resultsAmount: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var toolbar: MaterialToolbar
    private lateinit var backToTopButton: FloatingActionButton
    private lateinit var resultsLayout: LinearLayout

    // define layout
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_article, container, false)

        // Views
        recyclerView = view.findViewById(R.id.news_list)
        searchField = view.findViewById(R.id.searchField)
        searchIconButton = view.findViewById(R.id.search_icon_button)
        resultsAmount = view.findViewById(R.id.resultsAmount)
        progressBar = view.findViewById(R.id.progress_bar)
        toolbar = view.findViewById(R.id.toolbar)
        backToTopButton = view.findViewById(R.id.back_to_top_button)
        resultsLayout = view.findViewById(R.id.results_layout)

        resultsLayout.visibility = View.GONE
        backToTopButton.visibility = View.GONE
        backToTopButton.setImageResource(R.drawable.ic_baseline_arrow_upward_24)

        // Adapter and ViewModel
        adapter = NewsAdapter()
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        // Return the inflated layout as the root view of the fragment
        return view
    }

    // define the fragment's behavior
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.GONE
        val activity = requireActivity()

        setUpRecyclerView(activity)
        totalResultsObserver()
        observer()
        hideViewsWhenScrolled()

        searchIconButton.setOnClickListener {
            loadNewsArticlesOrShowProgressBar()
        }

        backToTopButton.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
            backToTopButton.visibility = View.GONE
        }
    }

    /*
        Observer - Using the 'articles' live data list from the view model and observing it.
        Then apply that list to the adapter to set the data to the Recycler View
     */

    private fun observer() {
        newsViewModel.articles.observe(viewLifecycleOwner){ listFromViewModel ->
            try {
                adapter.setNews(listFromViewModel)
            }
            catch (e: Exception){
                Log.d("ERROR", "$e")
            }
        }
    }

    private fun setUpRecyclerView(activity: FragmentActivity){
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun ifDataIsLoading(){
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

    private fun totalResultsObserver(){
        // were just using the results amount label to test the status data
        newsViewModel.totalResults.observe(viewLifecycleOwner){

            resultsAmount.text = it.toString()
        }
    }

    // Todo: Run an animation when hiding the views
    private fun hideViewsWhenScrolled(){
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

    private fun loadNewsArticlesOrShowProgressBar(){
        ifDataIsLoading()
        getArticles()
        activity?.hideKeyboard()
    }

    private fun getArticles() {
        val search = searchField.text.toString()
        if (search.isBlank()){
            Toast.makeText(
                this.context, "Please enter a search term",
                Toast.LENGTH_LONG
            ).show()
        } else {
            this.context?.let { context ->
                newsViewModel.fetchArticles(search, context)
                resultsLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun Activity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}