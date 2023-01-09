package com.samm.practiceapp01.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.samm.practiceapp01.R
import com.samm.practiceapp01.util.ViewUtility

class ArticleFragment : Fragment() {

    private val viewUtility = ViewUtility()
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchField: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var backToTopButton: FloatingActionButton
    private lateinit var layoutManager: LayoutManager
    private lateinit var errorMessageTV: TextView
    private var pageNumber = 1
    private var results = false
    private var observeResults: Boolean = false


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_article, container, false)
        val activity = requireActivity() // activity associated with this fragment

        recyclerView = view.findViewById(R.id.news_list)
        searchField = view.findViewById(R.id.search)
        progressBar = view.findViewById(R.id.progress_bar)
        backToTopButton = view.findViewById(R.id.back_to_top_button)
        errorMessageTV = view.findViewById(R.id.error_message_text_view)

        // Hide these view until search button is clicked
        backToTopButton.visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(activity)
        backToTopButton.setImageResource(R.drawable.ic_baseline_arrow_upward_24)

        adapter = NewsAdapter()
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        observeResults = observeResults(newsViewModel)

        return view
    }

    // define the fragment's behavior
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.GONE
        setUpRecyclerView(layoutManager)
        ifErrorMessage()

        newsViewModel.newsData(viewLifecycleOwner, adapter)
        viewUtility.hideViewsWhenScrolled(recyclerView, searchField, backToTopButton)

        backToTopButton.setOnClickListener { backToTopButtonClickListener() }

        // Get News Data when search button in keyboard is clicked
        searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    pageNumber = 1
                    showProgressBarOrLoadArticles(pageNumber, query)
                }
                return false
            }
            // Don't need this so just return false
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        recyclerView.isVerticalScrollBarEnabled = true
    }

    private fun setUpRecyclerView(layoutManager: LayoutManager){
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun showProgressBarOrLoadArticles(page: Int, search: String) {

        when {
            search.isEmpty() -> {
                Toast.makeText(
                    activity, "Please enter a search term", Toast.LENGTH_LONG
                ).show()
            }
            observeResults -> {
                Toast.makeText(
                    activity, "No Results Found", Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                // Get Data
                viewUtility.showProgressBarIfLoading(newsViewModel, viewLifecycleOwner, progressBar)
                newsViewModel.clearCache()
                newsViewModel.getArticles(page, search)
                viewUtility.hideKeyboard(activity)
            }
        }
    }

    private fun observeResults(viewModel: NewsViewModel): Boolean{
        viewModel.noResults.observe(viewLifecycleOwner){ noResults ->
            results = noResults
        }
        return results
    }

    private fun backToTopButtonClickListener() {
        recyclerView.smoothScrollToPosition(0)
        backToTopButton.visibility = View.GONE
    }

    // Don't know if this is really working
    private fun ifErrorMessage(){
        newsViewModel.error.observe(viewLifecycleOwner){ error ->
            errorMessageTV.text = error
            errorMessageTV.visibility = View.VISIBLE
        }
    }
}