package com.samm.practiceapp01.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.samm.practiceapp01.R
import com.samm.practiceapp01.core.ViewUtility

class ArticleFragment : Fragment() {

    val viewUtility = ViewUtility()
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchField: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var backToTopButton: FloatingActionButton
    private lateinit var layoutManager: LayoutManager
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
        progressBar = view.findViewById(R.id.progress_bar)
        backToTopButton = view.findViewById(R.id.back_to_top_button)
        errorMessageTV = view.findViewById(R.id.error_message_text_view)

        // Hide these view until search button is clicked
        backToTopButton.visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(activity)
        backToTopButton.setImageResource(R.drawable.ic_baseline_arrow_upward_24)

        adapter = NewsAdapter(requireContext())
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]


        return view
    }

    // define the fragment's behavior
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.GONE
        setUpRecyclerView(layoutManager)
        newsViewModel.newsData(viewLifecycleOwner, adapter)
        hideViewsWhenScrolled(recyclerView, searchField, backToTopButton)
        backToTopButton.setOnClickListener { backToTopButtonClickListener() }
        // Get News Data when search button in keyboard is clicked
        searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    pageNumber = 1
                    fetchNewsData(pageNumber, query)
                    ifErrorMessage()
                }
                return false
            }

            // Don't need this so just return false
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.action_bar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.clear -> {
                        newsViewModel.deleteAllAlertDialog(requireContext()){
                            newsViewModel.clearCache()
                            adapter.clearList()
                        }

                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        recyclerView.isVerticalScrollBarEnabled = true
    }


    private fun setUpRecyclerView(layoutManager: LayoutManager){
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun fetchNewsData(page: Int, search: String) {

        if (search.isEmpty()) {
            Toast.makeText(
                activity, "Please enter a search term", Toast.LENGTH_LONG
            ).show()
        } else {
            // Get Data
            observeLoading(newsViewModel, viewLifecycleOwner, progressBar, recyclerView)
            newsViewModel.getArticles(page, search)
            viewUtility.hideKeyboard(activity)
        }
    }


    private fun backToTopButtonClickListener() {
        recyclerView.smoothScrollToPosition(0)
        backToTopButton.visibility = View.GONE
    }

    // Don't know if this is really working
    private fun ifErrorMessage(){
        newsViewModel.error.observe(viewLifecycleOwner){ error ->
            errorMessageTV.visibility = View.VISIBLE
            errorMessageTV.text = error
        }
    }

    private fun hideViewsWhenScrolled(
        recyclerView: RecyclerView,
        toolbar: View,
        backToTopButton: FloatingActionButton
    ){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Get the first visible item position
                val firstVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager)
                    .findFirstVisibleItemPosition()

                // Check if the first visible item is the first item in the list
                if (firstVisibleItemPosition == 0 || recyclerView.isEmpty()) {
                    toolbar.visibility = View.VISIBLE
                    backToTopButton.visibility = View.GONE
                } else {
                    toolbar.visibility = View.GONE
                    backToTopButton.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun observeLoading(
        newsViewModel: NewsViewModel,
        viewLifecycleOwner: LifecycleOwner,
        progressBar: ProgressBar,
        recyclerView: RecyclerView
    ){
        newsViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE

            } else {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }
    }
}