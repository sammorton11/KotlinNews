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
import com.samm.practiceapp01.util.Observers
import com.samm.practiceapp01.util.ViewUtility

class ArticleFragment : Fragment() {

    private val viewUtility = ViewUtility()
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchField: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var backToTopButton: FloatingActionButton
    private lateinit var observers: Observers
    private lateinit var layoutManager: LayoutManager
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var errorMessageTV: TextView
    private var pageNumber = 1

    private val pageLimit = 50
    private var pageOffset = 0

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
        nextButton = view.findViewById(R.id.next_button)
        prevButton = view.findViewById(R.id.previous_button)
        errorMessageTV = view.findViewById(R.id.error_message_text_view)

        // Hide these view until search button is clicked
        backToTopButton.visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(activity)
        backToTopButton.setImageResource(R.drawable.ic_baseline_arrow_upward_24)

        adapter = NewsAdapter()
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        return view
    }

    // define the fragment's behavior
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.GONE
        setUpRecyclerView(layoutManager)
        val totalItemCount = recyclerView.adapter?.itemCount
        ifErrorMessage()
       // observers = activity?.let { _activity -> createObservers(_activity) }!!
        //newsViewModel.newsData(this, adapter)
        newsViewModel.newsDataByPage(pageLimit, pageOffset, this, adapter)

        viewUtility.hideViewsWhenScrolled(recyclerView, searchField, backToTopButton)
        nextButton.setOnClickListener { nextButtonClickListener() }
        prevButton.setOnClickListener { previousButtonClickListener() }
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
    }

    private fun clearCache(){
        newsViewModel.refreshCache()
    }

    private fun setUpRecyclerView(layoutManager: LayoutManager){
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun showProgressBarOrLoadArticles(page: Int, search: String) {
        viewUtility.showProgressBarIfLoading(newsViewModel, viewLifecycleOwner, progressBar)
        if (search.isEmpty()){
            Toast.makeText(
                activity, "Please enter a search term",
                Toast.LENGTH_LONG
            ).show()
        } else {
            clearCache()
            newsViewModel.getArticles(page, search)
            viewUtility.hideKeyboard(activity)
        }
    }

    private fun backToTopButtonClickListener() {
        recyclerView.smoothScrollToPosition(0)
        backToTopButton.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun nextButtonClickListener() {
        pageOffset += 10
        newsViewModel.newsDataByPage(pageLimit, pageNumber, this, adapter)

    }
    private fun previousButtonClickListener() {
        if (pageNumber != 0){
            pageNumber -= 10
            newsViewModel.newsDataByPage(pageLimit, pageNumber, this, adapter)
        }
    }

    private fun ifErrorMessage(){
        newsViewModel.error.observe(viewLifecycleOwner){ error ->
            errorMessageTV.text = error
            errorMessageTV.visibility = View.VISIBLE
        }
    }
}