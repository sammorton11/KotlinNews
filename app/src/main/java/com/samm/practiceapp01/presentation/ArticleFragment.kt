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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.samm.practiceapp01.R
import com.samm.practiceapp01.core.ViewUtility
import com.samm.practiceapp01.domain.models.Articles

class ArticleFragment : Fragment(), NewsAdapter.OnCardClick {

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
    private lateinit var navController: NavController

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

        layoutManager = LinearLayoutManager(activity)
        backToTopButton.setImageResource(R.drawable.ic_baseline_arrow_upward_24)

        adapter = NewsAdapter(requireContext(), this)
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.GONE
        setUpRecyclerView(layoutManager)
        navControllerSetup()
        newsViewModel.setNewsFromDatabase(viewLifecycleOwner, adapter)

        val menuHost: MenuHost = requireActivity()
        hideViewsWhenScrolled(recyclerView, searchField, backToTopButton)
        backToTopButton.setOnClickListener { backToTopButtonClickListener() }

        searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchNewsData(pageNumber, query)
                }
                return false
            }
            // Don't need this so just return false
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        // Action Menu -- Add a refresh button
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

    private fun navControllerSetup() {
        val navHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setUpRecyclerView(layoutManager: LayoutManager){
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun fetchNewsData(page: Int, search: String) {

        if (search.isEmpty()) {
            Toast.makeText(activity, "Search term is missing", Toast.LENGTH_LONG).show()
        } else {

            // Get Data from Database
            newsViewModel.fetchArticles(search, page)

            // Check if loading, success, or error
            newsViewModel.getState(this, adapter, progressBar, requireContext())
            viewUtility.hideKeyboard(activity)
        }
    }

    private fun backToTopButtonClickListener() {
        recyclerView.smoothScrollToPosition(0)
        backToTopButton.visibility = View.GONE
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

    override fun onCardClick(article: Articles?) {
        val bundle = Bundle()
        if (article != null) {
            bundle.putString("url", article.url)
        }
        navController.navigate(R.id.webViewFragment2, bundle)
    }
}