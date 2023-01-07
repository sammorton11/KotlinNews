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
import com.samm.practiceapp01.util.Constants.PAGE_SIZE
import com.samm.practiceapp01.util.Constants.pageAmount
import com.samm.practiceapp01.util.Observers
import com.samm.practiceapp01.util.Utility
import com.samm.practiceapp01.util.getPageAmount

class ArticleFragment : Fragment() {

    private val utility = Utility() // My utility class - todo: rename this
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchField: SearchView
    private lateinit var resultsAmountTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var backToTopButton: FloatingActionButton
    private lateinit var resultsLayout: LinearLayout
    private lateinit var observers: Observers
    private lateinit var layoutManager: LayoutManager
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var errorMessageTV: TextView
    private var pageNumber = 1

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
        resultsAmountTextView = view.findViewById(R.id.resultsAmount)
        progressBar = view.findViewById(R.id.progress_bar)
        backToTopButton = view.findViewById(R.id.back_to_top_button)
        resultsLayout = view.findViewById(R.id.results_layout)
        nextButton = view.findViewById(R.id.next_button)
        prevButton = view.findViewById(R.id.previous_button)
        errorMessageTV = view.findViewById(R.id.error_message_text_view)

        // Hide these view until search button is clicked
        resultsLayout.visibility = View.GONE
        backToTopButton.visibility = View.GONE
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
        ifErrorMessage()

        // Todo: Not sure if this will work
        observers = activity?.let {
            Observers(newsViewModel, viewLifecycleOwner, it.applicationContext).apply {
                allLiveDataObservers(resultsAmountTextView, searchField, adapter)
            }
        }!!

        utility.hideViewsWhenScrolled(resultsAmountTextView, recyclerView, searchField, backToTopButton)

        nextButton.setOnClickListener {
            nextButtonClickListener()
        }

        prevButton.setOnClickListener {
            previousButtonClickListener()
        }

        backToTopButton.setOnClickListener {
            backToTopButtonClickListener()
        }

        // Get News Data when search button in keyboard is clicked
        searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    pageNumber = 1
                    loadNewsArticlesOrShowProgressBar(pageNumber, query)
                }
                return false
            }
            // Don't need this so just return false
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setUpRecyclerView(layoutManager: LayoutManager){
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun loadNewsArticlesOrShowProgressBar(page: Int, search: String){
        utility.ifDataIsLoading(newsViewModel, viewLifecycleOwner, progressBar)
        activity.let { context ->
            activity?.applicationContext?.let {
                newsViewModel.getArticles(resultsLayout, page, search,
                    it)
            }
        }
        utility.hideKeyboard(activity)
    }

    private fun backToTopButtonClickListener() {
        recyclerView.smoothScrollToPosition(0)
        backToTopButton.visibility = View.GONE
    }

    private fun nextButtonClickListener() {
        if (pageNumber != getPageAmount(pageAmount, PAGE_SIZE)){
            pageNumber++
            observers.allLiveDataObservers(resultsAmountTextView, searchField, adapter)
            loadNewsArticlesOrShowProgressBar(pageNumber, searchField.query.toString())
        } else {
            pageNumber+=0
            Toast.makeText(activity, "Last Page", Toast.LENGTH_SHORT).show()
        }
    }
    private fun previousButtonClickListener() {
        if (pageNumber != 1){
            pageNumber--
            observers.allLiveDataObservers(resultsAmountTextView, searchField, adapter)
            loadNewsArticlesOrShowProgressBar(pageNumber, searchField.query.toString())
        }
    }

    private fun ifErrorMessage(){
        newsViewModel.error.observe(viewLifecycleOwner){ error ->
            errorMessageTV.text = error
            errorMessageTV.visibility = View.VISIBLE
        }
    }
}