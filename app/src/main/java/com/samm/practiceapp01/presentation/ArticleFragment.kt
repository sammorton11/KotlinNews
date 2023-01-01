package com.samm.practiceapp01.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.samm.practiceapp01.R
import com.samm.practiceapp01.util.Observers
import com.samm.practiceapp01.util.Utility

class ArticleFragment : Fragment() {

    private val utility = Utility()
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
    private lateinit var observers: Observers
    private lateinit var layoutManager: LayoutManager


    // define layout
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_article, container, false)
        val activity = requireActivity()
        // Views
        recyclerView = view.findViewById(R.id.news_list)
        searchField = view.findViewById(R.id.searchField)
        searchIconButton = view.findViewById(R.id.search_icon_button)
        resultsAmount = view.findViewById(R.id.resultsAmount)
        progressBar = view.findViewById(R.id.progress_bar)
        toolbar = view.findViewById(R.id.toolbar)
        backToTopButton = view.findViewById(R.id.back_to_top_button)
        resultsLayout = view.findViewById(R.id.results_layout)

        // Hide these view until search button is clicked
        resultsLayout.visibility = View.GONE
        backToTopButton.visibility = View.GONE
        layoutManager = LinearLayoutManager(activity)
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
        setUpRecyclerView(layoutManager)

        observers = Observers(newsViewModel, viewLifecycleOwner)
        observers.allLiveDataObservers(resultsAmount, toolbar, adapter)

        // do not hide toolbar if no results are found work around
        if (!resultsAmount.text.contains("0")){
            utility.hideViewsWhenScrolled(recyclerView, toolbar, backToTopButton)
        }


        searchIconButton.setOnClickListener {
            loadNewsArticlesOrShowProgressBar()
        }

        backToTopButton.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
            backToTopButton.visibility = View.GONE
        }
    }

    private fun setUpRecyclerView(layoutManager: LayoutManager){
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun loadNewsArticlesOrShowProgressBar(){
        utility.ifDataIsLoading(newsViewModel, viewLifecycleOwner, progressBar)
        this.context?.let { context ->
            newsViewModel.getArticles(searchField, resultsLayout, context)
        }
        utility.hideKeyboard(activity)
    }
}